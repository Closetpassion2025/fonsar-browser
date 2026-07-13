package com.cookiegames.smartcookie.security

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.cookiegames.smartcookie.browser.PasswordChoice
import com.cookiegames.smartcookie.di.UserPrefs
import com.cookiegames.smartcookie.preference.PASSWORD
import com.cookiegames.smartcookie.preference.PASSWORD_LOCK
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stores app and parental PINs as salted hashes inside [EncryptedSharedPreferences].
 * Legacy plaintext values in [UserPrefs] are migrated once on startup or first access.
 */
@Singleton
class PinCredentialStore @Inject constructor(
    context: Context,
    @UserPrefs private val legacyPreferences: SharedPreferences
) {

    enum class PinSlot {
        APP_LOCK,
        PARENTAL
    }

    private val securePreferences: SharedPreferences =
        createSecurePreferences(context.applicationContext, legacyPreferences)

    /**
     * Idempotent migration for users upgrading from plaintext PIN storage.
     * Safe to call on every cold start.
     */
    fun migrateLegacyPinsIfNeeded() {
        PinSlot.entries.forEach(::migrateLegacyPlaintextIfNeeded)
    }

    fun setPin(slot: PinSlot, pin: String) {
        if (pin.isEmpty()) {
            clearPin(slot)
            return
        }
        val salt = PinHasher.generateSalt()
        securePreferences.edit()
            .putString(hashKey(slot), PinHasher.hash(pin, salt))
            .putString(saltKey(slot), Base64.encodeToString(salt, Base64.NO_WRAP))
            .apply()
        clearLegacyPlaintext(slot)
    }

    fun verifyPin(slot: PinSlot, pin: String): Boolean {
        migrateLegacyPlaintextIfNeeded(slot)
        val storedHash = securePreferences.getString(hashKey(slot), null) ?: return false
        val saltEncoded = securePreferences.getString(saltKey(slot), null) ?: return false
        val salt = Base64.decode(saltEncoded, Base64.DEFAULT)
        return PinHasher.hash(pin, salt) == storedHash
    }

    fun hasPin(slot: PinSlot): Boolean {
        migrateLegacyPlaintextIfNeeded(slot)
        return securePreferences.contains(hashKey(slot))
    }

    fun clearPin(slot: PinSlot) {
        securePreferences.edit()
            .remove(hashKey(slot))
            .remove(saltKey(slot))
            .apply()
        clearLegacyPlaintext(slot)
    }

    private fun migrateLegacyPlaintextIfNeeded(slot: PinSlot) {
        val legacyKey = legacyKeyFor(slot)
        val plaintext = legacyPreferences.getString(legacyKey, null)?.takeIf { it.isNotEmpty() }
            ?: return
        if (securePreferences.contains(hashKey(slot))) {
            clearLegacyPlaintext(slot)
            return
        }
        setPin(slot, plaintext)
    }

    private fun clearLegacyPlaintext(slot: PinSlot) {
        legacyPreferences.edit().remove(legacyKeyFor(slot)).apply()
    }

    private fun legacyKeyFor(slot: PinSlot): String = when (slot) {
        PinSlot.APP_LOCK -> LEGACY_APP_LOCK_KEY
        PinSlot.PARENTAL -> LEGACY_PARENTAL_KEY
    }

    private fun hashKey(slot: PinSlot): String = when (slot) {
        PinSlot.APP_LOCK -> "app_lock_hash"
        PinSlot.PARENTAL -> "parental_hash"
    }

    private fun saltKey(slot: PinSlot): String = when (slot) {
        PinSlot.APP_LOCK -> "app_lock_salt"
        PinSlot.PARENTAL -> "parental_salt"
    }

    private fun createSecurePreferences(context: Context, settings: SharedPreferences): SharedPreferences {
        return try {
            openSecurePreferences(context)
        } catch (e: GeneralSecurityException) {
            Log.w(TAG, "Encrypted prefs failed; clearing corrupt crypto state and retrying", e)
            clearCorruptCryptoState(context, settings)
            openSecurePreferences(context)
        } catch (e: IOException) {
            Log.w(TAG, "Encrypted prefs failed; clearing corrupt crypto state and retrying", e)
            clearCorruptCryptoState(context, settings)
            openSecurePreferences(context)
        }
    }

    companion object {
        private const val TAG = "PinCredentialStore"
        private const val SECURE_PREFS_NAME = "pin_credentials"
        private const val LEGACY_APP_LOCK_KEY = "usePasswordLock"
        private const val LEGACY_PARENTAL_KEY = "usePassword"
        private const val MASTER_KEY_ALIAS = "_androidx_security_master_key_"

        private fun openSecurePreferences(context: Context): SharedPreferences {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            return EncryptedSharedPreferences.create(
                context,
                SECURE_PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        /**
         * Recovery for unusable Android Keystore master keys (Tink #535).
         * Clears encrypted prefs and the Keystore alias so a fresh keyset can be
         * created, and resets the app-lock / parental flags so the user is not
         * locked out by an orphaned flag. Stored PIN hashes are lost; users must
         * set PINs again.
         */
        internal fun clearCorruptCryptoState(context: Context, settings: SharedPreferences) {
            // Log.println is not covered by the -assumenosideeffects Log rule, so this
            // recovery event stays visible in release builds for field diagnosis.
            Log.println(Log.WARN, TAG, "Recovering from corrupt encrypted PIN storage: clearing keyset and master key; stored PINs are lost")
            context.deleteSharedPreferences(SECURE_PREFS_NAME)
            settings.edit()
                .putInt(PASSWORD_LOCK, PasswordChoice.NONE.value)
                .putInt(PASSWORD, PasswordChoice.NONE.value)
                .apply()
            try {
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)
                if (keyStore.containsAlias(MASTER_KEY_ALIAS)) {
                    keyStore.deleteEntry(MASTER_KEY_ALIAS)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to delete master key alias from Android Keystore", e)
            }
        }
    }
}
