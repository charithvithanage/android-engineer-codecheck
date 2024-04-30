package jp.co.yumemi.android.code_check.ui.customdialogs

import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentCustomProgressDialogBinding
import jp.co.yumemi.android.code_check.utils.UIUtils.Companion.changeUiSize

/**
 * CustomProgressDialogFragment is a custom implementation of a progress dialog as an
 * Android DialogFragment.
 *
 * This dialog provides a customizable progress indicator with an optional message.
 * It disables the dismiss event on back button press and offers the ability to set a custom message.
 *
 * @constructor Creates a new instance of [CustomProgressDialogFragment].
 * @property binding The binding class responsible for inflating the dialog's layout.
 */
class CustomProgressDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomProgressDialogBinding

    companion object {
        private const val ARG_MESSAGE = "message"

        /**
         * Create a new instance of CustomProgressDialogFragment with an optional message.
         *
         * @param message The message to be displayed in the progress dialog.
         * @return An instance of CustomProgressDialogFragment.
         */
        fun newInstance(
            message: String?
        ): CustomProgressDialogFragment {
            val fragment = CustomProgressDialogFragment()
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //Disable back button pressed dialog dismiss event
        isCancelable = false
        FragmentCustomProgressDialogBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@CustomProgressDialogFragment
            resources.configuration.apply {
                setDialogBackground(uiMode)
            }
        }
        return binding.root
    }

    private fun setDialogBackground(mode: Int) {
        binding.dialogMainLayout.apply {
            when (mode and Configuration.UI_MODE_NIGHT_MASK) {
                UI_MODE_NIGHT_YES -> {
                    setBackgroundResource(
                        R.drawable.dark_dialog_content_border_bg
                    )
                }

                else -> {
                    setBackgroundResource(
                        R.drawable.dialog_content_border_bg
                    )
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the message from arguments and set it to the data binding variables
        arguments?.getString(ARG_MESSAGE).let { message ->
            binding.apply {
                //Dialog Width with horizontal margin
                changeUiSize(context, dialogMainLayout, 1, 1, 30)
                //Icon width=(Device Width/3)
                changeUiSize(context, icon, 1, 5)
                // Set data to the data binding variables
                binding.dialogMessage = message
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Check the new night mode and set the background accordingly
        setDialogBackground(newConfig.uiMode)
    }

}