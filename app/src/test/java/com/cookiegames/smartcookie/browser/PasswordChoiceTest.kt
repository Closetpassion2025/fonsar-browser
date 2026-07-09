package com.cookiegames.smartcookie.browser

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for app lock choices. Run in Android Studio via
 * right-click → Run 'PasswordChoiceTest'.
 */
class PasswordChoiceTest {

    @Test
    fun enumValues_areStableForExistingUsers() {
        // Persisted preference indices must never change
        assertEquals(0, PasswordChoice.NONE.value)
        assertEquals(1, PasswordChoice.CUSTOM.value)
        assertEquals(2, PasswordChoice.BIOMETRIC.value)
    }

    @Test
    fun fromPersistedValue_mapsLegacyCustomLock() {
        val choice = PasswordChoice.values().first { it.value == 1 }
        assertEquals(PasswordChoice.CUSTOM, choice)
    }

    @Test
    fun fromPersistedValue_mapsBiometricLock() {
        val choice = PasswordChoice.values().first { it.value == 2 }
        assertEquals(PasswordChoice.BIOMETRIC, choice)
    }
}
