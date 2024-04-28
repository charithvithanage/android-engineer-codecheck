/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
//            setupNavController()
//            setSupportActionBar(toolbar)
            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
//                setFragmentName(LocalHelper.getString(this@MainActivity, R.string.menu_home))
            }

//            btnBack.setOnClickListener {
//                onBackPressed()
//            }

            // Get the current configuration
            // Get the current configuration
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            // Check if it's in night mode

            // Check if it's in night mode
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                // Set dark mode background image
                mainLayout.setBackgroundResource(R.mipmap.night_bg)
            } else {
                // Set light mode background image
                mainLayout.setBackgroundResource(R.mipmap.bg)
            }
        }
    }
}
