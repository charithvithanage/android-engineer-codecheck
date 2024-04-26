/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
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


//            setupNavController()
//            setSupportActionBar(toolbar)
            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
//                setFragmentName(LocalHelper.getString(this@MainActivity, R.string.menu_home))
            }

//            btnBack.setOnClickListener {
//                onBackPressed()
//            }

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
                Configuration.UI_MODE_NIGHT_YES -> setBackgroundResource(
                    R.mipmap.night_bg
                )
                else -> setBackgroundResource(R.mipmap.bg)
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
