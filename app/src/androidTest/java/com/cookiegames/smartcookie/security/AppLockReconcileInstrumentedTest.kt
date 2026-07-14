package com.cookiegames.smartcookie.security

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cookiegames.smartcookie.MainActivity
import com.cookiegames.smartcookie.R
import com.cookiegames.smartcookie.browser.PasswordChoice
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * P5-01: orphaned app-lock flag (CUSTOM) with no stored PIN hash must self-heal on launch.
 */
@RunWith(AndroidJUnit4::class)
class AppLockReconcileInstrumentedTest {

    private lateinit var context: Context
    private lateinit var store: PinCredentialStore

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val legacyPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        store = PinCredentialStore(context, legacyPrefs)
        store.clearPin(PinCredentialStore.PinSlot.APP_LOCK)
        legacyPrefs.edit()
            .putInt("passwordLock", PasswordChoice.CUSTOM.value)
            .remove("usePasswordLock")
            .commit()
    }

    @Test
    fun reconcileAppLockState_customFlagWithoutPinHash_resetsToNone() {
        ActivityScenario.launch(MainActivity::class.java).use {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            assertEquals(
                PasswordChoice.NONE.value,
                prefs.getInt("passwordLock", PasswordChoice.NONE.value)
            )
            onView(withId(R.id.search)).check(matches(isDisplayed()))
        }
    }
}
