/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.FragmentFavouritesBinding
import jp.co.yumemi.android.code_check.models.LocalGitHubRepoObject
import jp.co.yumemi.android.code_check.ui.customdialogs.ConfirmDialogButtonClickListener
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel
import jp.co.yumemi.android.code_check.utils.DialogUtils

/**
 * FavouriteFragment is responsible for displaying the user's favorite GitHub repositories.
 *
 * This fragment includes a RecyclerView that shows a list of favorite repositories. Users can
 * interact with each repository item, expanding or collapsing additional details. They can also
 * delete a repository from their favorites.
 *
 * @property binding View binding for this fragment's layout
 * @property viewModel View model for managing repository details and favorite status
 * @property sharedViewModel View model shared with the main activity
 * @property favouriteListAdapter Adapter for the RecyclerView, managing the display of favorite
 * repositories
 *
 */
@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: FavouritesViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var favouriteListAdapter: FavouriteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflating the layout using View Binding
        FragmentFavouritesBinding.inflate(inflater, container, false).apply {
            binding = this
            // Initializing FavouritesViewModel using ViewModelProvider
            ViewModelProvider(requireActivity())[FavouritesViewModel::class.java].apply {
                viewModel = this
                vm = this
            }
            // Shared ViewModel to communicate layout changes with the MainActivity
            sharedViewModel =
                ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
                    setFragment(StringConstants.FAVOURITE_FRAGMENT)
                }

            lifecycleOwner = this@FavouritesFragment
        }
        return binding.root
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state.
     */
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
        sharedViewModel.apply {
            viewModel.apply {
                binding.apply {
                    /**
                     * Initializes the RecyclerView adapter for displaying favorite repositories.
                     * The adapter handles item click events and delete icon click events.
                     */
                    FavouriteListAdapter(object : FavouriteListAdapter.OnItemClickListener {
                        override fun itemClick(item: LocalGitHubRepoObject, isExpanded: Boolean) {
                            expandedStates[item.id] = !isExpanded
                        }

                        override fun deleteIconClick(item: LocalGitHubRepoObject) {
                            // Show a confirmation dialog before deleting a favorite repository
                            DialogUtils.showConfirmAlertDialog(
                                requireActivity(),
                                getString(
                                    R.string.remove_fav_confirmation_message
                                ),
                                object : ConfirmDialogButtonClickListener {
                                    override fun onPositiveButtonClick() {
                                        viewModel.deleteFavourite(item.id)
                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                }
                            )
                        }
                    }).apply {
                        favouriteListAdapter = this
                        /* Set Adapter to Recycle View */
                        binding.recyclerView.also { it2 ->
                            it2.adapter = this
                        }

                        // Update the adapter with the expanded states from the shared ViewModel
                        updateStatus(sharedViewModel.expandedStates)
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

    }

    /**
     * Observes changes in the ViewModel's live data and updates the RecyclerView accordingly.
     * Also handles the visibility of the empty data image.
     */
    private fun viewModelObservers() {
        // Observer to catch list data and update the RecyclerView using DiffUtils
        viewModel.allFavourites?.observe(requireActivity()) { list ->
            sharedViewModel.apply {
                list?.let {
                    favouriteListAdapter.submitList(it)
                    when {
                        it.isEmpty() -> setEmptyDataImage(true)
                        else -> setEmptyDataImage(false)
                    }
                }
            }
        }
    }
}