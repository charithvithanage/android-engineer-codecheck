package jp.co.yumemi.android.code_check.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constants.StringConstants.ENGLISH
import jp.co.yumemi.android.code_check.constants.StringConstants.JAPANESE
import javax.inject.Inject

/**
 * Settings Fragment View Model
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {

    private val _selectedLanguage = MutableLiveData<String?>(null)
    val selectedLanguage get() = _selectedLanguage

    private val _shouldSelectEnglish = MutableLiveData<Boolean?>(null)
    val shouldSelectEnglish get() = _shouldSelectEnglish

    private val _shouldSelectJapanese = MutableLiveData<Boolean?>(null)
    val shouldSelectJapanese get() = _shouldSelectJapanese

    private val _titleLabel = MutableLiveData<String?>(null)
    val titleLabel get() = _titleLabel

    private val _languageLabel = MutableLiveData<String?>(null)
    val languageLabel get() = _languageLabel

    /**
     * Sets the selected language and updates related LiveData values.
     *
     * @param language The code representing the selected language.
     */
    fun setSelectedLanguage(language: String?) {
        _selectedLanguage.value = language
        run {
            _shouldSelectEnglish.value = language.equals(ENGLISH)
            _shouldSelectJapanese.value = language.equals(JAPANESE)
        }
    }

    /**
     * Sets the label associated with the selected language.
     *
     * @param titleLabel The label to be associated with the selected language.
     * @param languageLabel The label to be associated with the selected button text.
     */
    fun setSelectedLanguageLabels(
        titleLabel: String?,
        languageLabel: String?
    ) {
        _titleLabel.value = titleLabel
        _languageLabel.value = languageLabel
    }

    /**
     * Handles the click event for the English layout, setting the selected language to English.
     */
    fun onEnglishLayoutClicked() {
        // Handle the click event for the English layout
        _selectedLanguage.value = ENGLISH
        _shouldSelectJapanese.value = false
        _shouldSelectEnglish.value = true
    }

    /**
     * Handles the click event for the Japanese layout, setting the selected language to Japanese.
     */
    fun onJapaneseLayoutClicked() {
        // Handle the click event for the Japanese layout
        _selectedLanguage.value = JAPANESE
        _shouldSelectJapanese.value = true
        _shouldSelectEnglish.value = false
    }
}