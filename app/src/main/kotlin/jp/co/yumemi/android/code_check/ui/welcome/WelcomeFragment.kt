package jp.co.yumemi.android.code_check.ui.welcome

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.StringConstants
import jp.co.yumemi.android.code_check.databinding.FragmentWelcomeBinding
import jp.co.yumemi.android.code_check.ui.main.MainActivityViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    //Main Activity view model
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.WELCOME_FRAGMENT)
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
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {

        }
    }
}