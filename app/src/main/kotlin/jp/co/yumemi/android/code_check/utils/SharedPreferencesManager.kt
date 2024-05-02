package jp.co.yumemi.android.code_check.utils

import android.content.SharedPreferences
import jp.co.yumemi.android.code_check.constants.PreferenceKeys.LANGUAGE
import jp.co.yumemi.android.code_check.constants.StringConstants.ENGLISH
import jp.co.yumemi.android.code_check.interfaces.SuccessListener
import jp.co.yumemi.android.code_check.utils.SharedPreferencesManager.Companion.sharedPreferences

/**
 * A utility class for managing shared preferences.
 *
 * @property sharedPreferences The shared preferences instance.
 */
class SharedPreferencesManager {

    companion object {
        private var sharedPreferences: SharedPreferences? = null

        /**
         * Initialize the SharedPreferencesManager with the provided SharedPreferences instance.
         *
         * @param sharedPreferences The SharedPreferences instance to be used for storing preferences.
         */
        fun init(sharedPreferences: SharedPreferences) {
            this.sharedPreferences =
                sharedPreferences

        }

        /**
         * Get the selected language value from the shared preferences.
         *
         * Default language is English.
         *
         * @return The selected language code. Returns [StringConstants.ENGLISH] if not set.
         */
        fun getSelectedLanguage() =
            sharedPreferences?.getString(LANGUAGE, ENGLISH)

        /**
         * Update the language preference value in the shared preferences.
         *
         * @param jsonString The new language code to be stored.
         */
        fun updateSelectedLanguage(
            jsonString: String?
        ) {
            sharedPreferences?.edit()?.run {
                putString(LANGUAGE, jsonString)
                apply()
            }
        }

        /**
         * Retrieves a boolean preference value associated with the given [key] from the SharedPreferences.
         *
         * @param key The key used to retrieve the boolean value.
         * @return The boolean value associated with the key, or `null` if the key is not found or if SharedPreferences is null.
         */
        fun getPreferenceBool(key: String): Boolean? =
            sharedPreferences?.getBoolean(key, false)

        /**
         * Saves a boolean preference value associated with the given [key] to the SharedPreferences.
         *
         * @param key The key used to save the boolean value.
         * @param value The boolean value to be saved.
         * @param successListener An optional listener to be notified upon successful saving of the preference. Default is `null`.
         */
        fun savePreferenceBool(
            key: String?,
            value: Boolean, successListener: SuccessListener? = null
        ) {
            sharedPreferences?.edit()?.run {
                putBoolean(key, value)
                apply()
                successListener?.onSuccess()
            }
        }
    }
}
