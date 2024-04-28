/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.main

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_SQUARE
import android.content.res.Configuration.ORIENTATION_UNDEFINED
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.ActivityMainBinding
import jp.co.yumemi.android.code_check.utils.LanguageManager


/**
 * The main activity of the app, responsible for managing the UI and navigation.
 * This activity hosts various fragments and handles user interactions.
 *
 * This activity serves as the entry point to the app and handles the display of various
 * fragments and navigation through the bottom navigation menu.
 *
 * @property sharedViewModel The shared view model for communicating data and state between fragments.
 * @property binding The data binding object that allows for easy interaction with the layout XML.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(this).loadLanguage()
        DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            binding = this
            setupNavController()

            resources.configuration.apply {
                // Check the initial device orientation and set the menu accordingly
                setMenuVisibility(orientation)
                // Check the initial night mode and set the background accordingly
                setBackGroundImage(uiMode)
            }

            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
                vm = this
            }

            viewModelObservers()
        }
    }

    /**
     * Set up the navigation controller for the bottom navigation menu.
     */
    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navHostFragment.navController.let { navController ->

            if (binding.bottomNavigationMenu != null) {
                binding.bottomNavigationMenu!!.apply {
                    bottomNavView = this
                    this@MainActivity.menu = this.menu
                    setupWithNavController(navController)
                }
            }

        }
    }

    /**
     * Observers for LiveData changes in the shared view model.
     */
    private fun viewModelObservers() {
        sharedViewModel.apply {
            /**
             * Observes changes in the sharedViewModel's fragment LiveData and updates the UI elements
             * in the MainActivity accordingly.
             *
             */

            binding.apply {
                fragment.observe(this@MainActivity) {
                    // Set the title text based on the observed fragment
                    when (it) {
                        StringConstants.WELCOME_FRAGMENT -> {
                            toolbar.isVisible = false
                            title.isVisible = false
                            bottomNavigationMenu?.isVisible = false
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        }

                        StringConstants.HOME_FRAGMENT -> {
                            sharedViewModel.setFragmentName(getString(R.string.menu_home))
                            toolbar.isVisible = true
                            title.isVisible = true
                            bottomNavigationMenu?.isVisible = true
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        }

                        StringConstants.FAVOURITE_FRAGMENT -> {
                            sharedViewModel.setFragmentName(getString(R.string.menu_favourites))
                        }

                        StringConstants.SETTINGS_FRAGMENT -> {
                            sharedViewModel.setFragmentName(getString(R.string.menu_settings))
                        }
                    }
                }

                /**
                 * Observe changes in a LiveData and update the action bar title of the MainActivity accordingly.
                 * Set live data value when the navigate through fragments
                 */
                fragmentName.observe(this@MainActivity) {
                    title.text = it
                }

                /**
                 * Observe changes in a LiveData and update the bottom menu of the MainActivity accordingly.
                 *
                 * @param @MainActivity The current MainActivity instance where this code is executed.
                 */
                updateLabels.observe(this@MainActivity) {
                    binding.bottomNavigationMenu?.let {
                        updateMenuValues(this@MainActivity, it)
                    }
//                    updateSideValues(this@MainActivity, binding.drawerSideMenu.root)
                }

            }

        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Check the new device orientation and set the menu accordingly
        setMenuVisibility(newConfig.orientation)

        // Check the new night mode and set the background accordingly
        setBackGroundImage(newConfig.uiMode)
    }

    private fun updateMenuValues(context: Context?, bottomNavigationView: BottomNavigationView) {
        context?.let {
            bottomNavigationView.menu.let {
                it.findItem(R.id.homeFragment).title = getString(R.string.menu_home)
                it.findItem(R.id.favouritesFragment).title = getString(R.string.menu_favourites)
                it.findItem(R.id.settingsFragment).title = getString(R.string.menu_settings)
            }
        }
    }

    private fun setBackGroundImage(mode: Int) {
        binding.mainLayout.apply {
            // Check if it's in night mode
            // Set dark mode background image
            // Set light mode background image
            when (mode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    setBackgroundResource(
                        R.mipmap.night_bg
                    )

                    binding.drawerSideMenu?.mainLayout?.setBackgroundColor(Color.parseColor("#000000"))
                }

                else -> {
                    setBackgroundResource(R.mipmap.bg)
                    binding.drawerSideMenu?.mainLayout?.setBackgroundColor(Color.parseColor("#ffffff"))

                }
            }
        }
    }

    private fun setMenuVisibility(orientation: Int) {
        binding.leftButton.apply {
            when (orientation) {
                ORIENTATION_PORTRAIT -> {
                    isVisible = false
                }

                ORIENTATION_LANDSCAPE -> {
                    isVisible = true
                    setImageResource(R.drawable.hamburger)
                }

                ORIENTATION_SQUARE -> {
                    isVisible = false
                }

                ORIENTATION_UNDEFINED -> {
                    isVisible = false
                }
            }
        }
    }
}
