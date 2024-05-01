/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.FragmentHomeBinding
import jp.co.yumemi.android.code_check.models.GitHubRepoObject
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel
import jp.co.yumemi.android.code_check.utils.NetworkUtils

/**
 * HomeFragment for displaying a list of GitHub repositories and handling user interactions.
 *
 * This fragment is responsible for initializing the user interface, setting up listeners,
 * and observing LiveData updates from the associated [HomeViewModel]. Allows the user to search for
 * repositories by entering a search term in a search view and displays a list of GitHub
 * repositories and . Additionally, it handles the navigation to the RepoDetailsFragment
 * when a repository item is clicked.
 *
 * @constructor Creates an instance of [HomeFragment].
 *
 * @property binding Binding for the fragment layout
 * @property viewModel ViewModel for the HomeFragment
 * @property sharedViewModel Shared ViewModel for communication with the MainActivity
 * @property repoListAdapter Adapter for the list of GitHub repositories
 * @property dialog Dialog fragment for showing error messages and progress
 * @property dialogVisibleObserver Observer for handling dialog visibility
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var repoListAdapter: RepoListAdapter
    private var dialog: DialogFragment? = null
    private lateinit var dialogVisibleObserver: Observer<String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflating the layout using View Binding
        FragmentHomeBinding.inflate(inflater, container, false).apply {
            binding = this
            // Initializing HomeViewModel using ViewModelProvider
            ViewModelProvider(requireActivity())[HomeViewModel::class.java].apply {
                viewModel = this
                vm = this
            }
            // Setting the lifecycle owner for data binding
            lifecycleOwner = this@HomeFragment
        }
        // Initializing and setting up MainActivityViewModel
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.HOME_FRAGMENT)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModelObservers()
    }

    /**
     * Initializes the user interface, sets up listeners, and performs language-specific
     * configurations.
     */
    private fun initView() {
        // Setting the search view hint based on the localized string
        viewModel.apply {
            // Setting up the searchInputText's OnEditorActionListener
            binding.apply {
                searchInputText.setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_SEARCH -> {
                            val enteredValue: String? = searchViewText

                            sharedViewModel.apply {
                                //Empty value error Alert
                                when {
                                    enteredValue.isNullOrEmpty() ->
                                        showErrorDialog(getString(R.string.search_input_empty_error))

                                    else -> when {
                                        NetworkUtils.isNetworkAvailable() -> {
                                            setProgressDialogVisible(true)
                                            getGitHubRepoList(enteredValue)
                                        }

                                        else ->
                                            showErrorDialog(getString(R.string.network_error))
                                    }
                                }
                            }

                            true
                        }

                        else -> false
                    }
                }
                // Initializing RepoListAdapter and setting it to RecyclerView
                RepoListAdapter(
                    object : RepoListAdapter.OnItemClickListener {
                        override fun itemClick(item: GitHubRepoObject) {
                            navigateToRepositoryFragment(item)
                        }
                    }).apply {
                    repoListAdapter = this
                    /* Set Adapter to Recycle View */
                    recyclerView.also { it2 ->
                        it2.adapter = this
                    }
                }

            }
        }

        // Handle back button press for Home Fragment
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.setExitConfirmationDialogVisible(true)
            }
        }.apply {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, this)
        }
    }

    /**
     * Observes LiveData updates from the ViewModel and updates the UI accordingly.
     */
    private fun viewModelObservers() {
        sharedViewModel.apply {
            // Observer to catch list data
            // Update RecyclerView Items using DiffUtils
            viewModel.gitHubRepoList.observe(requireActivity()) { repoList ->
                setProgressDialogVisible(false)
                repoList?.let {
                    repoListAdapter.submitList(it)
                    when {
                        it.isEmpty() -> sharedViewModel.setEmptyDataImage(true)
                        else -> sharedViewModel.setEmptyDataImage(false)
                    }
                }
            }

            viewModel.errorMessage.observe(requireActivity()) { message ->
                setProgressDialogVisible(false)
                showErrorDialog(message)
            }
        }
    }

    /**
     * Navigates to the RepoDetailsFragment when a GitHub repository item is clicked.
     *
     * @param gitHubRepo The selected GitHub repository object.
     */
    fun navigateToRepositoryFragment(gitHubRepo: GitHubRepoObject) {
        findNavController().navigate(
            HomeFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(
                gitHubRepo
            )
        )
    }
}