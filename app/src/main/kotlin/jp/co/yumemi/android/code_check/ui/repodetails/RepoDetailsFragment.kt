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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants.ACCOUNT_DETAILS_FRAGMENT
import jp.co.yumemi.android.code_check.databinding.FragmentRepoDetailsBinding
import jp.co.yumemi.android.code_check.models.GitHubRepoObject
import jp.co.yumemi.android.code_check.models.LocalDBQueryResponse
import jp.co.yumemi.android.code_check.ui.customdialogs.ConfirmDialogButtonClickListener
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel
import jp.co.yumemi.android.code_check.utils.DialogUtils
import jp.co.yumemi.android.code_check.utils.SingleLiveEvent.Companion.observeOnce

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
 * @property sharedViewModel View model shared with the main activity
 * @property localDBResponseObserver Observer for handling responses from local database operations
 */
class RepoDetailsFragment : Fragment() {
    private val args: RepoDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentRepoDetailsBinding
    private lateinit var viewModel: RepoDetailsViewModel
    private lateinit var gitHubRepo: GitHubRepoObject
    private lateinit var sharedViewModel: MainActivityViewModel
    private var localDBResponseObserver: Observer<LocalDBQueryResponse>? = null
    private var isFavourite: Boolean = false

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

                // If savedInstanceState is null, set isFavourite from the arguments.
                // If savedInstanceState is not null, get isFavourite from the ViewModel.
                isFavourite =
                    when (savedInstanceState) {
                        null -> args.isFavourite
                        else -> favouriteStatus.value ?: false
                    }
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

                btnFav.setOnClickListener {
                    val confirmationMessage = favouriteStatus.value?.let {
                        if (it) {
                            R.string.remove_fav_confirmation_message
                        } else {
                            R.string.add_fav_confirmation_message
                        }
                    } ?: R.string.add_fav_confirmation_message

                    DialogUtils.showConfirmAlertDialog(
                        requireActivity(),
                        getString(confirmationMessage),
                        object : ConfirmDialogButtonClickListener {
                            override fun onPositiveButtonClick() {
                                when (favouriteStatus.value) {
                                    true -> deleteFavourite(gitHubRepo.id)
                                    else -> addToFavourites()
                                }
                            }

                            override fun onNegativeButtonClick() {
                            }
                        }
                    )
                }

                btnMore.setOnClickListener {
                    navigateToWebProfileFragment()
                }

            }

            setGitRepoData(gitHubRepo)
            checkFavStatus(isFavourite)
        }

        //Handle back pressed event
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }.apply {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, this)
        }
    }

    /**
     * Initialize ViewModel observers.
     */
    private fun viewModelObservers() {

        /* According to the response show alert dialog(Error or Success) */
        localDBResponseObserver = Observer { response ->
            when {
                response.success -> sharedViewModel.showSuccessDialog(response.message)

                else -> sharedViewModel.showErrorDialog(response.message)
            }
        }

        /**
         * Success dialog will only be shown once when the localDBResponse LiveData is triggered,
         * even if rotate the device. This ensures that the dialog doesn't reappear on device rotation.
         * Observe the LiveData using a helper function observeOnce
         */
        localDBResponseObserver?.let {
            viewModel.localDBResponse.observeOnce(viewLifecycleOwner, it)
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

    /**
     * Called when the view previously created by [onCreateView] has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        localDBResponseObserver?.let { observer ->
            // Remove the observer when the Fragment is destroyed
            viewModel.localDBResponse.removeObserver(observer)
        }
    }
}
