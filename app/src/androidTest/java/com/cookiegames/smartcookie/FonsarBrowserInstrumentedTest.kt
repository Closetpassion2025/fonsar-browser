package com.cookiegames.smartcookie

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.appcompat.widget.Toolbar
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
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
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.startActivity(
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        onView(withId(R.id.search))
                .check(matches(isDisplayed()))
    }

    @Test
    fun settings_backArrowReturnsToRootFromSubScreen() {
        ActivityScenario.launch(SettingsActivity::class.java).use {
            onView(allOf(
                    withText(R.string.settings_general),
                    hasSibling(withText(R.string.settings_general_explain))
            )).perform(click())

            onView(withText(R.string.translator))
                    .check(matches(isDisplayed()))

            onView(withId(R.id.toolbar))
                    .perform(clickToolbarNavigationIcon())

            onView(withText(R.string.settings_about))
                    .check(matches(isDisplayed()))
        }
    }

    @Test
    fun settings_systemBackReturnsToRootFromSubScreen() {
        ActivityScenario.launch(SettingsActivity::class.java).use {
            onView(allOf(
                    withText(R.string.settings_general),
                    hasSibling(withText(R.string.settings_general_explain))
            )).perform(click())

            onView(withText(R.string.translator))
                    .check(matches(isDisplayed()))

            pressBack()

            onView(withText(R.string.settings_about))
                    .check(matches(isDisplayed()))
        }
    }

    private fun clickToolbarNavigationIcon(): ViewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(Toolbar::class.java)

        override fun getDescription(): String = "click toolbar navigation icon"

        override fun perform(uiController: UiController, view: View) {
            val toolbar = view as Toolbar
            for (index in 0 until toolbar.childCount) {
                val child = toolbar.getChildAt(index)
                if (child.isClickable && child.contentDescription != null) {
                    child.performClick()
                    return
                }
            }
            throw PerformException.Builder()
                    .withActionDescription(description)
                    .withViewDescription("Toolbar navigation icon")
                    .build()
        }
    }
}
