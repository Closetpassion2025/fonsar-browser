/*
 * Copyright 2014 A.C.R. Development
 */
package com.cookiegames.smartcookie.settings.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cookiegames.smartcookie.AppTheme
import com.cookiegames.smartcookie.R
import com.cookiegames.smartcookie.di.injector
import com.cookiegames.smartcookie.preference.UserPreferences
import com.cookiegames.smartcookie.settings.fragment.SettingsFragment
import javax.inject.Inject


class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Inject
    internal lateinit var userPreferences: UserPreferences

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        when (userPreferences.useTheme) {
            AppTheme.LIGHT -> setTheme(R.style.Theme_SettingsTheme)
            AppTheme.DARK -> setTheme(R.style.Theme_SettingsTheme_Dark)
            AppTheme.BLACK -> setTheme(R.style.Theme_SettingsTheme_Black)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById(R.id.toolbar)

        try {
            setSupportActionBar(toolbar)
            val actionBar: ActionBar? = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false)
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setDisplayShowHomeEnabled(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        toolbar.title = getString(R.string.settings)
        toolbar.setNavigationOnClickListener { navigateUp() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateUp()
            }
        })

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarTitle()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingsFragment())
                .commit()
        }

        overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out_scale)
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val fragmentClass = pref.fragment ?: return false
        val fragment: Fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            fragmentClass
        )
        fragment.arguments = pref.extras
        fragment.setTargetFragment(caller, 0)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(pref.title?.toString())
            .commit()
        toolbar.title = pref.title
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateUp() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    private fun updateToolbarTitle() {
        toolbar.title = if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name ?: getString(R.string.settings)
        } else {
            getString(R.string.settings)
        }
    }
}
