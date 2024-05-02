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
import jp.co.yumemi.android.code_check.constants.ImageResources
import jp.co.yumemi.android.code_check.constants.ImageResources.GIT_ACCOUNT_SEARCH_IMAGE_CODE
import jp.co.yumemi.android.code_check.constants.ImageResources.NO_DATA_IMAGE_CODE
import jp.co.yumemi.android.code_check.constants.ImageResources.getImageResources
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.ActivityMainBinding
import jp.co.yumemi.android.code_check.databinding.SideMenuBinding
import jp.co.yumemi.android.code_check.ui.customdialogs.ConfirmDialogButtonClickListener
import jp.co.yumemi.android.code_check.ui.customdialogs.CustomAlertDialogListener
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.FAIL
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.SUCCESS
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.WARN
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.showAlertDialog
import jp.co.yumemi.android.code_check.utils.DialogUtils.Companion.showConfirmAlertDialog
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

            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
                vm = this
                resources.configuration.apply {
                    // Check the initial device orientation and set the menu accordingly
                    setMenuVisibility(orientation)
                }
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
                            sharedViewModel.setExitConfirmationDialogVisible(true)
                        }
                    }
                }
            }

            leftButton.setOnClickListener {
                if (sharedViewModel.showHamburgerMenu.value == true) {
                    drawerLayout?.openDrawer(GravityCompat.START)
                } else {
                    navController.popBackStack()
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
                    // Set the title text based on the observed fragment
                    when (it) {
                        StringConstants.WELCOME_FRAGMENT -> {
                            setEmptyDataImage(false)
                            toolbar.isVisible = false
                            title.isVisible = false
                            bottomNavigationMenu?.isVisible = false
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        }

                        StringConstants.HOME_FRAGMENT -> {
                            setEmptyDataImage(true)
                            showHamburgerMenu(true)
                            sharedViewModel.setFragmentName(getString(R.string.menu_home))
                            toolbar.isVisible = true
                            title.isVisible = true
                            bottomNavigationMenu?.isVisible = true
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        }

                        StringConstants.ACCOUNT_DETAILS_FRAGMENT -> {
                            setEmptyDataImage(false)
                            bottomNavigationMenu?.isVisible = false
                            showHamburgerMenu(false)
                            leftButton.isVisible = true
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                            sharedViewModel.setFragmentName(getString(R.string.account_details))
                        }

                        StringConstants.WEB_PROFILE_VIEW_FRAGMENT -> {
                            setEmptyDataImage(false)
                            bottomNavigationMenu?.isVisible = false
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            leftButton.isVisible = true
                            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                            sharedViewModel.setFragmentName(getString(R.string.web_profile))
                        }

                        StringConstants.FAVOURITE_FRAGMENT -> {
                            setEmptyDataImage(true)
                            showHamburgerMenu(true)
                            sharedViewModel.setFragmentName(getString(R.string.menu_favourites))
                        }

                        StringConstants.SETTINGS_FRAGMENT -> {
                            setEmptyDataImage(false)
                            showHamburgerMenu(true)
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
                        showAlertDialog(
                            this@MainActivity,
                            FAIL,
                            errorMessage, object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    sharedViewModel.showErrorDialog(null)
                                }
                            }
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
                        showAlertDialog(
                            this@MainActivity,
                            SUCCESS,
                            msg, object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    sharedViewModel.showSuccessDialog(null)
                                }
                            }
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
                        showAlertDialog(
                            this@MainActivity,
                            WARN,
                            message, object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    sharedViewModel.showWarnDialog(null)
                                }
                            }
                        )
                    }
                }

                /**
                 * Observe changes in a LiveData and update the bottom menu of the MainActivity accordingly.
                 *
                 * @param @MainActivity The current MainActivity instance where this code is executed.
                 */
                updateLabels.observe(this@MainActivity) {
                    bottomNavigationMenu?.menu?.let {
                        it.findItem(R.id.exitMenu).setOnMenuItemClickListener {
                            sharedViewModel.setExitConfirmationDialogVisible(true)
                            true
                        }
                    }

                    drawerSideMenu?.let {
                        updateSideMenuValues(this@MainActivity, it)
                    }
                }

                /**
                 * Observes the LiveData [showHamburgerMenu] in the MainActivity and updates the visibility of the hamburger menu button accordingly.
                 * If [isVisible] is true, the hamburger menu button is displayed; otherwise, the button displays a left arrow.
                 */
                showHamburgerMenu.observe(this@MainActivity) { isVisible ->
                    if (isVisible) {
                        leftButton.setImageResource(R.drawable.hamburger)
                    } else {
                        leftButton.setImageResource(R.drawable.left_arrow)
                    }

                    resources.configuration.apply {
                        // Check the initial device orientation and set the menu accordingly
                        setMenuVisibility(orientation)
                    }
                }

                existConfirmationDialogVisible.observe(this@MainActivity) { isVisible ->
                    if (isVisible) {
                        showConfirmAlertDialog(this@MainActivity,
                            getString(R.string.exit_confirmation_message),
                            object : ConfirmDialogButtonClickListener {
                                override fun onPositiveButtonClick() {
                                    finish()
                                }

                                override fun onNegativeButtonClick() {
                                    // Reset the LiveData here so that it does not trigger again
                                    setExitConfirmationDialogVisible(false)
                                }
                            }
                        )
                        // Reset the LiveData here so that it does not trigger again
                        setExitConfirmationDialogVisible(false)
                    }
                }

                /**
                 * Observes the [sharedViewModel.isSearchResultsEmpty] LiveData to handle changes in the
                 * search results' emptiness.
                 *
                 * This method observes the LiveData and updates the visibility and image resource of
                 * an [ImageView] based on whether the search results are empty or not.
                 *
                 * @param isSearchResultsEmpty The LiveData indicating whether the search results are empty.
                 *   - If `null`, the [ImageView] is shown with a default search account image.
                 *   - If `true`, the [ImageView] is shown with an image specific to the fragment,
                 *     or a default no data image for other fragments.
                 *   - If `false`, the [ImageView] is hidden.
                 */
                isSearchResultsEmpty.observe(this@MainActivity) { isSearchResultsEmpty ->
                    if (fragment.value == StringConstants.HOME_FRAGMENT || fragment.value == StringConstants.FAVOURITE_FRAGMENT) {
                        isSearchResultsEmpty?.let { isEmpty ->
                            binding.emptyImageView.isVisible = isEmpty
                            if (isEmpty) {
                                binding.emptyImageView.setImageResource(
                                    when (fragment.value) {
                                        // In the Home Fragment, show an account search image
                                        StringConstants.HOME_FRAGMENT -> getImageResources(
                                            GIT_ACCOUNT_SEARCH_IMAGE_CODE
                                        )
                                        // For other Fragments, show a no data image according to the language
                                        else -> ImageResources.getImageResources(
                                            NO_DATA_IMAGE_CODE
                                        )
                                    }
                                )
                            }
                        } ?: run {
                            // If the LiveData value is null, show the ImageView with a search account image
                            binding.emptyImageView.isVisible = true
                            binding.emptyImageView.setImageResource(R.mipmap.search_account)
                        }
                    } else {
                        binding.emptyImageView.isVisible = false
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
                    sharedViewModel.fragment.value.apply {
                        isVisible = this == StringConstants.ACCOUNT_DETAILS_FRAGMENT ||
                                this == StringConstants.WEB_PROFILE_VIEW_FRAGMENT
                    }


                }

                ORIENTATION_LANDSCAPE -> {
                    isVisible = true
                    sharedViewModel.fragment.value.apply {
                        setImageResource(
                            if (this == StringConstants.ACCOUNT_DETAILS_FRAGMENT ||
                                this == StringConstants.WEB_PROFILE_VIEW_FRAGMENT
                            ) {
                                R.drawable.left_arrow
                            } else {
                                R.drawable.hamburger
                            }
                        )
                    }
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
