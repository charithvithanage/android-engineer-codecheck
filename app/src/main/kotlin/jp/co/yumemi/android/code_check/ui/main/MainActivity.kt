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
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.ActivityMainBinding
import jp.co.yumemi.android.code_check.databinding.SideMenuBinding
import jp.co.yumemi.android.code_check.utils.DialogUtils
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.FAIL
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.SUCCESS
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.WARN
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.showAlertDialogWithoutAction
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.showProgressDialog
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
    private lateinit var navController: NavController
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(this).loadLanguage()

        DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            binding = this
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            setupNavController()

            resources.configuration.apply {
                // Check the initial device orientation and set the menu accordingly
                setMenuVisibility(orientation)
            }

            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
                vm = this
            }

            drawerSideMenu?.let {
                it.apply {
                    drawerLayout?.apply {
                        closeBtn.setOnClickListener {
                            closeDrawer(GravityCompat.START)

                        }

                        homeButtonLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            navController.navigate(R.id.homeFragment)
                        }

                        favButtonLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            navController.navigate(R.id.favouritesFragment)

                        }

                        settingsButtonLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            navController.navigate(R.id.settingsFragment)
                        }

                        logoutButtonLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                        }

                        leftButton.setOnClickListener {
                            if (sharedViewModel.showHamburgerMenu.value == true) {
                                openDrawer(GravityCompat.START)
                            } else {
                                navController.popBackStack()
                            }
                        }
                    }
                }
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
                    showHamburgerMenu(true)
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

                /* Live data observer to show/hide progress dialog */
                isProgressDialogVisible.observe(this@MainActivity) { isVisible ->
                    isVisible?.let { showDialog ->
                        when {
                            // If the showDialog == true, show the dialog else dismiss the dialog
                            showDialog -> {
                                dialog?.dismiss()
                                dialog = showProgressDialog(
                                    this@MainActivity, getString(R.string.progressing)
                                )
                            }

                            else -> dialog?.dismiss()
                        }
                    }
                }

                /**
                 * Observes changes in the errorMessage LiveData and triggers a dialog to display any non-null error messages.
                 * If an error message is received, dismisses any existing dialog and displays a new AlertDialog
                 * with the error message.
                 */
                errorMessage.observe(this@MainActivity) { errorMessage ->
                    errorMessage?.let {
                        dialog?.dismiss()
                        showAlertDialogWithoutAction(
                            this@MainActivity,
                            FAIL,
                            errorMessage
                        )
                    }
                }

                /**
                 * Observes changes in the success message LiveData and displays an alert dialog if a non-null success message is received.
                 *
                 * @param successMessage LiveData containing success messages.
                 */
                successMessage.observe(this@MainActivity) { message ->
                    message?.let { msg ->
                        dialog?.dismiss()
                        showAlertDialogWithoutAction(
                            this@MainActivity,
                            SUCCESS,
                            msg
                        )
                    }
                }

                /**
                 * Observes changes in the warning message LiveData and displays an alert dialog if a non-null warning message is received.
                 *
                 * @param warnMessage LiveData containing warning messages.
                 */
                warnMessage.observe(this@MainActivity) { message ->
                    message?.let {
                        dialog?.dismiss()
                        showAlertDialogWithoutAction(
                            this@MainActivity,
                            WARN,
                            message
                        )
                    }
                }


                /**
                 * Observe changes in a LiveData and update the bottom menu of the MainActivity accordingly.
                 *
                 * @param @MainActivity The current MainActivity instance where this code is executed.
                 */
                updateLabels.observe(this@MainActivity) {
                    binding.bottomNavigationMenu?.let {
                        updateBottomMenuValues(this@MainActivity, it)
                    }

                    binding.drawerSideMenu?.let {
                        updateSideMenuValues(this@MainActivity, it)
                    }
                }

                /**
                 * Observes the LiveData [showHamburgerMenu] in the MainActivity and updates the visibility of the hamburger menu button accordingly.
                 * If [isVisible] is true, the hamburger menu button is displayed; otherwise, the button displays a left arrow.
                 */
                showHamburgerMenu.observe(this@MainActivity) { isVisible ->
                    if (isVisible) {
                        binding.leftButton.setImageResource(R.drawable.hamburger)
                    } else {
                        binding.leftButton.setImageResource(R.drawable.left_arrow)
                    }
                }

            }

        }
    }

    /**
     * Updates the text labels of the side menu based on the current locale.
     *
     * This function retrieves localized strings for menu items such as Home, Favourites, Settings, and Exit,
     * and applies them to the respective TextViews in the side menu. This ensures that the side menu
     * displays the correct labels according to the current language settings.
     *
     * @param context The context used to access resources, necessary for retrieving localized strings.
     * @param sideMenuBinding The binding object for the side menu layout, which provides direct access
     *                        to the TextViews that need updating.
     */
    private fun updateSideMenuValues(context: Context?, sideMenuBinding: SideMenuBinding) {
        context?.let {
            sideMenuBinding.apply {
                homeLabel.text = getString(R.string.menu_home)
                favMenuLabel.text = getString(R.string.menu_favourites)
                settingsLabel.text = getString(R.string.menu_settings)
                logoutLabel.text = getString(R.string.exit)
            }
        }
    }

    /**
     * Updates the text values of menu items in the provided [bottomNavigationView] based on the current context.
     *
     * @param context The context used to retrieve string resources for menu item text.
     * @param bottomNavigationView The BottomNavigationView whose menu items need to be updated.
     */
    private fun updateBottomMenuValues(
        context: Context?,
        bottomNavigationView: BottomNavigationView
    ) {
        context?.let {
            bottomNavigationView.menu.let {
                it.findItem(R.id.homeFragment).title = getString(R.string.menu_home)
                it.findItem(R.id.favouritesFragment).title = getString(R.string.menu_favourites)
                it.findItem(R.id.settingsFragment).title = getString(R.string.menu_settings)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Check the new device orientation and set the menu accordingly
        setMenuVisibility(newConfig.orientation)
    }

    /**
     * Sets the visibility and image resource of the left button based on the device orientation.
     *
     * @param orientation The device orientation as an integer value (ORIENTATION_PORTRAIT, ORIENTATION_LANDSCAPE, ORIENTATION_SQUARE, ORIENTATION_UNDEFINED).
     */
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
