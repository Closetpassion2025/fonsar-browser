package com.cookiegames.smartcookie

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cookiegames.smartcookie.browser.PasswordChoice
import com.cookiegames.smartcookie.settings.activity.SettingsActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for Fonsar Browser. Run in Android Studio with an emulator
 * or device: right-click → Run 'FonsarBrowserInstrumentedTest'.
 *
 * Gradle: ./gradlew connectedScMainDebugAndroidTest
 */
@RunWith(AndroidJUnit4::class)
class FonsarBrowserInstrumentedTest {

    @Before
    fun disableAppLock() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("settings", 0)
                .edit()
                .putInt("passwordLock", PasswordChoice.NONE.value)
                .apply()
    }

    @Test
    fun appLaunches_andShowsSearchBar() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.search))
                    .check(matches(isDisplayed()))
        }
    }

    @Test
    fun settings_privacySectionShowsAppLock() {
        ActivityScenario.launch(SettingsActivity::class.java).use {
            onView(withText(R.string.settings_privacy))
                    .perform(click())

            onView(withId(androidx.preference.R.id.recycler_view))
                    .perform(
                            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                                    hasDescendant(withText(R.string.app_lock))
                            )
                    )

            onView(withText(R.string.app_lock))
                    .check(matches(isDisplayed()))
        }
    }
}
