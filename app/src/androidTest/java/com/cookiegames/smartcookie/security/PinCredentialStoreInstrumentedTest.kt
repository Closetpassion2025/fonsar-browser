package com.cookiegames.smartcookie.security

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Verifies EncryptedSharedPreferences round-trip after security-crypto upgrades.
 * Uses the same pref file name and MasterKey scheme as production pin_credentials.
 */
@RunWith(AndroidJUnit4::class)
class PinCredentialStoreInstrumentedTest {

    private lateinit var store: PinCredentialStore

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val legacyPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        store = PinCredentialStore(context, legacyPrefs)
    }

    @Test
    fun setPin_verifyPin_roundTrip() {
        store.setPin(PinCredentialStore.PinSlot.APP_LOCK, "2468")
        assertTrue(store.verifyPin(PinCredentialStore.PinSlot.APP_LOCK, "2468"))
        assertFalse(store.verifyPin(PinCredentialStore.PinSlot.APP_LOCK, "0000"))
    }

    @Test
    fun hasPin_reflectsStoredState() {
        store.clearPin(PinCredentialStore.PinSlot.PARENTAL)
        assertFalse(store.hasPin(PinCredentialStore.PinSlot.PARENTAL))

        store.setPin(PinCredentialStore.PinSlot.PARENTAL, "1357")
        assertTrue(store.hasPin(PinCredentialStore.PinSlot.PARENTAL))
    }
}
