/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.repodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants.ACCOUNT_DETAILS_FRAGMENT
import jp.co.yumemi.android.code_check.constants.StringConstants.HOME_FRAGMENT
import jp.co.yumemi.android.code_check.databinding.FragmentRepoDetailsBinding
import jp.co.yumemi.android.code_check.models.GitHubRepoObject
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel

/**
 * Fragment that displays details of a GitHub repository and allows users to mark it as a favorite.
 *
 * This fragment is responsible for showing detailed information about a selected GitHub repository,
 * allowing users to mark it as a favorite, and handling back navigation to the Home Fragment. It
 * communicates with the [RepoDetailsViewModel] to manage the repository data and favorite status.
 *
 * @property args Arguments received from the navigation component, including the GitHub repository item
 * @property binding View binding for this fragment's layout
 * @property viewModel View model for managing repository details and favorite status
 * @property gitHubRepo The selected GitHub repository to be displayed
 * @property isFavourite Flag indicating whether the repository is marked as a favorite
 * @property sharedViewModel View model shared with the main activity
 * @property localDBResponseObserver Observer for handling responses from local database operations
 */
class RepoDetailsFragment : Fragment() {
    private val args: RepoDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentRepoDetailsBinding
    private lateinit var viewModel: RepoDetailsViewModel
    private lateinit var gitHubRepo: GitHubRepoObject
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Safe Args: Retrieve the GitHub repository object
        gitHubRepo = args.item
        FragmentRepoDetailsBinding.inflate(inflater, container, false).apply {
            binding = this
            ViewModelProvider(requireActivity())[RepoDetailsViewModel::class.java].apply {
                viewModel = this
                vm = this
            }
            lifecycleOwner = this@RepoDetailsFragment
        }

        // Initialize the shared ViewModel with the main activity
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(ACCOUNT_DETAILS_FRAGMENT)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModelObservers()
    }

    /**
     * Initialize ViewModel observers.
     */
    private fun viewModelObservers() {

    }

    /**
     * Initialize the view components.
     */
    private fun initView() {
        viewModel.apply {
            binding.apply {

                requireActivity().apply {
                    //Update Report Details Resource values with localization
                    updateUI(
                        getString(R.string.view_more),
                        getString(R.string.starts),
                        getString(R.string.forks),
                        getString(R.string.watchers),
                        getString(R.string.open_issues),
                        getString(R.string.language)
                    )
                }

                btnMore.setOnClickListener {
                    navigateToWebProfileFragment()
                }

            }

            setGitRepoData(gitHubRepo)
            sharedViewModel.setFragment(ACCOUNT_DETAILS_FRAGMENT)
        }

        //Handle back pressed event
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homeFragment)
                // Handle back button press for Home Fragment
                sharedViewModel.setFragment(HOME_FRAGMENT)
            }
        }.apply {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, this)
        }
    }

    /**
     * Navigates to the WebProfileViewFragment using the Navigation Component.
     * If the owner's HTML URL is available, it constructs the appropriate navigation action.
     * If the HTML URL is null, the navigation will not be performed.
     */
    private fun navigateToWebProfileFragment() {
        findNavController().navigate(
            gitHubRepo.owner?.htmlUrl.let {
                RepoDetailsFragmentDirections.actionRepoDetailsFragmentToWebProfileViewFragment(
                    it
                )
            }
        )
    }
}
