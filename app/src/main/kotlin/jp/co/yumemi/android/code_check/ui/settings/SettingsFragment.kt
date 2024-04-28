package jp.co.yumemi.android.code_check.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.co.yumemi.android.code_check.constants.StringConstants.SETTINGS_FRAGMENT
import jp.co.yumemi.android.code_check.databinding.FragmentSettingsBinding
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel
import jp.co.yumemi.android.code_check.utils.LanguageManager

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    //Main Activity view model
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@SettingsFragment
            ViewModelProvider(requireActivity())[SettingsViewModel::class.java].apply {
                viewModel = this
                vm = this
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(SETTINGS_FRAGMENT)
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