package jp.co.yumemi.android.code_check.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import jp.co.yumemi.android.code_check.ui.customdialogs.CustomAlertDialogFragment

/**
 * Utility class for managing custom dialogs in application.
 * This class provides methods to show custom alert dialogs and progress dialogs.
 */
class DialogUtils {
    /**
     * A companion object to provide static methods for creating custom dialogs.
     */
    companion object {

        /**
         * Represents a success dialog.
         */
        const val SUCCESS = "success"

        /**
         * Represents a fail dialog.
         */
        const val FAIL = "fail"

        /**
         * Represents a warn dialog.
         */
        const val WARN = "warn"

        /**
         * Represents the tag for a custom alert dialog fragment.
         */
        const val ALERT_DIALOG_FRAGMENT_TAG = "CustomAlertDialogFragmentTag"


        /**
         * Show a custom alert dialog without any button click event.
         *
         * @param context The context in which the dialog should be shown.
         * @param type The type of the dialog (Success, Fail, or Warn Alert).
         * @param message The message body to be displayed in the dialog.
         */
        fun showAlertDialogWithoutAction(
            context: Context, type: String, message: String?
        ) {
            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message, type).show(
                    fragmentManager,
                    ALERT_DIALOG_FRAGMENT_TAG
                )
            }
        }

    }


}