/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.main

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(this).loadLanguage()
        DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            binding = this

            resources.configuration.apply {
                // Check the initial device orientation and set the menu accordingly
                setMenuVisibility(orientation)
                // Check the initial night mode and set the background accordingly
                setBackGroundImage(uiMode)
            }

            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
            }

            viewModelObservers()
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
             * @param fragment The LiveData that represents the current fragment.
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
                            toolbar.isVisible = true
                            title.isVisible = true
                            bottomNavigationMenu?.isVisible = true
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                        }
                    }
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
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                // Code to show the bottom menu
//                binding.bottomNavigationMenu.isVisible = true
//                binding.drawerSideMenu.isVisible = false
                Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show()
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                // Code to show the side menu
//                binding.bottomNavigationMenu.isVisible = false
//                binding.drawerSideMenu.isVisible = true
//                Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
