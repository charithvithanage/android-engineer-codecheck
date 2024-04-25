package jp.co.yumemi.android.code_check.utils

import android.content.Context
import android.content.res.Configuration
import jp.co.yumemi.android.code_check.utils.SharedPreferencesManager.Companion.getSelectedLanguage
import java.util.Locale

/**
 * LanguageManager class.
 * Manages the language settings.
 * @param context The context used for language operations.
 */
class LanguageManager(private val context: Context) {
    /**
     * Loads the selected language.
     */
    fun loadLanguage() {
        val language = getSelectedLanguage()
        setLocale(language)
    }

    /**
     * Sets the locale for the application.
     * @param language The language code to set.
     */
    private fun setLocale(language: String?) {
        language?.let {
            val locale = Locale(it)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }
    }
}