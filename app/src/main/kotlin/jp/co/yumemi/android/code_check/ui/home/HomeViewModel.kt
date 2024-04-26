package jp.co.yumemi.android.code_check.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for managing data and business logic related to the home screen.
 *
 * This class provides methods and LiveData objects to interact with GitHub repository data and manage
 * the user interface state.
 *
 * @param gitHubRepository The [GitHubRepository] responsible for fetching GitHub data.
 * @param localGitHubRepository The [LocalGitHubRepository] responsible for handling local database operations.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
) :
    ViewModel() {

    //Search View Hint Text
    private val _searchViewHint = MutableLiveData<String>(null)
    val searchViewHint get() = _searchViewHint

    //User entered text
    var searchViewText: String? = null


    /**
     * Set a hint text for the search view.
     *
     * This function updates the [_searchViewHint] LiveData with the provided [hint] text, which serves
     * as a hint for the search view in the user interface.
     *
     * @param hint The hint text to be displayed in the search view.
     */
    fun setSearchViewHint(hint: String) {
        _searchViewHint.value = hint
    }
}