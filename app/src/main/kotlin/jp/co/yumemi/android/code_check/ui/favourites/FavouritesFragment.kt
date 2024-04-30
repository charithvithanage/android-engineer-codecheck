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
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.FragmentFavouritesBinding
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: FavouritesViewModel
    private lateinit var sharedViewModel: MainActivityViewModel

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
            // Setting the lifecycle owner for data binding
            lifecycleOwner = this@FavouritesFragment
        }
        // Initializing and setting up MainActivityViewModel
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.FAVOURITE_FRAGMENT)
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

    }
}