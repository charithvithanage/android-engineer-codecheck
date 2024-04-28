package jp.co.yumemi.android.code_check.ui.welcome

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.PreferenceKeys.APP_LAUNCHED_STATUS
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.FragmentWelcomeBinding
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel
import jp.co.yumemi.android.code_check.utils.LanguageManager
import jp.co.yumemi.android.code_check.utils.SharedPreferencesManager


/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    //Main Activity view model
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var binding: FragmentWelcomeBinding
    lateinit var viewModel: WelcomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            SharedPreferencesManager.getPreferenceBool(APP_LAUNCHED_STATUS)
                ?.let {
                    if(it) {
                        //Already launched the app
                        navigateToHomeFragment()
                    }else{
                        sharedViewModel = this
                        setFragment(StringConstants.WELCOME_FRAGMENT)
                    }

                } ?: run {
                //If app is launched for the first time
                sharedViewModel = this
                setFragment(StringConstants.WELCOME_FRAGMENT)
            }
        }

        FragmentWelcomeBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@WelcomeFragment

            // Check the current color mode
            val currentColorMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            // Set the background drawable based on the color mode
            if (currentColorMode == Configuration.UI_MODE_NIGHT_YES) {
                button.setBackgroundResource(R.drawable.dark_theme_button_background)
            } else {
                button.setBackgroundResource(R.drawable.button_background)
            }

            ViewModelProvider(this@WelcomeFragment)[WelcomeFragmentViewModel::class.java].apply {
                viewModel = this
                vm = this

                SharedPreferencesManager.getSelectedLanguage().apply {
                    setSelectedLanguage(
                        this
                    )
                }

                selectedLanguage.observe(requireActivity()) {
                    // Update the selected value in the preference
                    SharedPreferencesManager.updateSelectedLanguage(it)
                    // Set language preference
                    val languageManager = LanguageManager(requireContext())
                    languageManager.loadLanguage()
                    setSelectedLanguageLabels(
                        getString(R.string.welcomePageTitle),
                        getString(R.string.welcomePageContent),
                        getString(R.string.next)
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            SharedPreferencesManager.savePreferenceBool(
                APP_LAUNCHED_STATUS,
                true
            )
            navigateToHomeFragment()
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
        )
    }
}