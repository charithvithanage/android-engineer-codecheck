package jp.co.yumemi.android.code_check.constants

import android.content.Context
import android.content.SharedPreferences
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.utils.LanguageManager
import jp.co.yumemi.android.code_check.utils.SharedPreferencesManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImageResourcesTest {
    private lateinit var languageManager: LanguageManager
    @Mock
    private lateinit var context: Context
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        languageManager = LanguageManager(context)
        val sharedPreferences = Mockito.mock<SharedPreferences>()

        // Initialize SharedPreferencesManager with the mocked SharedPreferences
        SharedPreferencesManager.init(sharedPreferences)
    }
    @Test
    fun `test getImageResources with Git account search image code`() {
        val expectedResourceId = R.mipmap.search_account
        val actualResourceId = ImageResources.getImageResources(ImageResources.GIT_ACCOUNT_SEARCH_IMAGE_CODE)
        assertEquals(expectedResourceId, actualResourceId)
    }

    @Test
    fun `test getImageResources with No Data image code (English)`() {
        val selectedLanguage = StringConstants.ENGLISH
        Mockito.`when`(SharedPreferencesManager.getSelectedLanguage()).thenReturn(selectedLanguage)

        val expectedResourceId = R.mipmap.no_data_en
        val actualResourceId = ImageResources.getImageResources(ImageResources.NO_DATA_IMAGE_CODE)
        assertEquals(expectedResourceId, actualResourceId)
    }

    @Test
    fun `test getImageResources with No Data image code (Japanese)`() {
        val selectedLanguage = StringConstants.JAPANESE
        Mockito.`when`(SharedPreferencesManager.getSelectedLanguage()).thenReturn(selectedLanguage)

        val expectedResourceId = R.mipmap.no_data_jp
        val actualResourceId = ImageResources.getImageResources(ImageResources.NO_DATA_IMAGE_CODE)
        assertEquals(expectedResourceId, actualResourceId)

        // Reset the selected language after the test
        SharedPreferencesManager.updateSelectedLanguage(StringConstants.ENGLISH)
    }

    @Test
    fun `test getImageResources with invalid image code`() {
        val expectedResourceId = 0
        val actualResourceId = ImageResources.getImageResources(999) // Invalid image code
        assertEquals(expectedResourceId, actualResourceId)
    }
}