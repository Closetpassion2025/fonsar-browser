package com.cookiegames.smartcookie.preference

import android.content.Context
import com.cookiegames.smartcookie.browser.PasswordChoice
import com.cookiegames.smartcookie.security.PinCredentialStore

private const val LEGACY_PREFS_NAME = "com.cookiegames.smartcookie"
private const val LEGACY_NO_PASSWORD_KEY = "noPassword"

/**
 * One-time migration from the upstream package-name SharedPreferences file.
 */
fun migrateLegacyPackagePreferences(
    context: Context,
    userPreferences: UserPreferences,
    pinCredentialStore: PinCredentialStore
) {
    val legacy = context.getSharedPreferences(LEGACY_PREFS_NAME, Context.MODE_PRIVATE)
    if (!legacy.contains(LEGACY_NO_PASSWORD_KEY)) {
        return
    }

    val parentalPasswordEnabled = !legacy.getBoolean(LEGACY_NO_PASSWORD_KEY, true)
    if (parentalPasswordEnabled &&
        userPreferences.passwordChoice == PasswordChoice.NONE &&
        pinCredentialStore.hasPin(PinCredentialStore.PinSlot.PARENTAL)
    ) {
        userPreferences.passwordChoice = PasswordChoice.CUSTOM
    }

    legacy.edit().remove(LEGACY_NO_PASSWORD_KEY).apply()
}
