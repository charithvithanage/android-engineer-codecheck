package jp.co.yumemi.android.code_check.ui.main

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

    /**
     * Sets the currently displayed fragment.
     *
     * @param fragment The string identifier of the fragment.
     */
    fun setFragment(fragment: String) {
        _fragment.value = fragment
    }
}