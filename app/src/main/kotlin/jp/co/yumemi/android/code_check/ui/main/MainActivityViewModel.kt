package jp.co.yumemi.android.code_check.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constants.StringConstants.WELCOME_FRAGMENT
import javax.inject.Inject

/**
 * ViewModel for the main activity of your application.
 *
 * This ViewModel is responsible for managing the state and data related to the main activity,
 * including the currently displayed fragment, the status of the bottom menu, expanded states,
 * and whether search results are empty.
 *
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor() :
    ViewModel() {

    private val _fragment = MutableLiveData(WELCOME_FRAGMENT)
    val fragment get() = _fragment

    private val _fragmentName = MutableLiveData("")
    val fragmentName get() = _fragmentName

    private val _updateLabels = MutableLiveData(true)
    val updateLabels: LiveData<Boolean> get() = _updateLabels

    private val _showHamburgerMenu = MutableLiveData(true)
    val showHamburgerMenu get() = _showHamburgerMenu

    /**
     * Sets the currently displayed fragment.
     *
     * @param fragment The string identifier of the fragment.
     */
    fun setFragment(fragment: String) {
        _fragment.value = fragment
    }

    /**
     * @param fragmentName The name of the fragment.
     */
    fun setFragmentName(fragmentName: String) {
        _fragmentName.value = fragmentName
    }

    /**
     * Sets the update status of the bottom menu.
     *
     * @param isUpdateStatus A boolean indicating whether the bottom menu status should be updated.
     */
    fun setUpdateLabels(isUpdateStatus: Boolean) {
        _updateLabels.value = isUpdateStatus
    }

    /**
     * Sets the visibility status of the hamburger menu.
     *
     * @param showStatus A boolean value indicating whether the hamburger menu should be shown (`true`) or hidden (`false`).
     */
    fun showHamburgerMenu(showStatus: Boolean) {
        _showHamburgerMenu.value = showStatus
    }
}