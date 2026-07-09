package com.cookiegames.smartcookie.security

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.cookiegames.smartcookie.di.UserPrefs
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

    private val securePreferences: SharedPreferences = createSecurePreferences(context)

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

    companion object {
        private const val SECURE_PREFS_NAME = "pin_credentials"
        private const val LEGACY_APP_LOCK_KEY = "usePasswordLock"
        private const val LEGACY_PARENTAL_KEY = "usePassword"

        private fun createSecurePreferences(context: Context): SharedPreferences {
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
    }
}
