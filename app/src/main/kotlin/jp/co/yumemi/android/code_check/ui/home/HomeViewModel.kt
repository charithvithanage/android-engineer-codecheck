package jp.co.yumemi.android.code_check.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.models.GitHubRepoObject
import jp.co.yumemi.android.code_check.repositories.GitHubRepository
import jp.co.yumemi.android.code_check.repositories.LocalGitHubRepository
import kotlinx.coroutines.launch
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
    private val gitHubRepository: GitHubRepository,
    private val localGitHubRepository: LocalGitHubRepository
) :
    ViewModel() {

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    /**
     * LiveData representing a list of all favourite GitHub repositories from the local database.
     */
    val allFavourites = localGitHubRepository.getAllRepositories()

    //User entered text
    var searchViewText: String? = null

    //Git Hub Repo List from the server
    private val _gitHubRepoList = MutableLiveData<List<GitHubRepoObject>?>()
    val gitHubRepoList get() = _gitHubRepoList


    /**
     * Get the server response and set values to LiveData.
     *
     * This function fetches GitHub repository data based on the provided [inputText] and updates the
     * [_gitHubRepoList] LiveData with the retrieved data. It also handles errors by updating the
     * [_errorMessage] LiveData. Additionally, it manages the visibility of a progress dialog with the
     *
     * @param inputText The text entered by the user for repository search.
     */
    fun getGitHubRepoList(inputText: String?) {
        //Show Progress Dialog when click on the search view submit button

        /* View Model Scope - Coroutine */
        viewModelScope.launch {
            gitHubRepository.getRepositories(inputText).apply {
                if (success) {
                    _gitHubRepoList.value = items
                } else {
                    _errorMessage.value = message
                }
            }
        }
    }
}