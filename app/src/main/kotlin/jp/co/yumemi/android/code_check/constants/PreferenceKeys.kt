package jp.co.yumemi.android.code_check.constants

/**
 * A singleton object that contains keys for accessing shared preferences in the app.
 * Use these keys to store and retrieve data from SharedPreferences related to preferences.
 */
object PreferenceKeys {
    /**
     * Key for storing and retrieving the selected language preference.
     * The value associated with this key represents the language code.
     */
    const val LANGUAGE = "language"

    /**
     * Key for storing and retrieving the status of the app launch.
     * The value associated with this key indicates whether the app has been launched previously.
     */
    const val APP_LAUNCHED_STATUS = "launch_status"
}