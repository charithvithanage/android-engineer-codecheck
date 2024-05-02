package jp.co.yumemi.android.code_check.utils

import android.content.SharedPreferences
import jp.co.yumemi.android.code_check.constants.PreferenceKeys
import jp.co.yumemi.android.code_check.constants.StringConstants.JAPANESE
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for SharedPreferencesManager.
 *
 * @RunWith(MockitoJUnitRunner::class) is used to indicate that MockitoJUnitRunner should be used to run the tests.
 */
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesManagerTest {

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // Mock SharedPreferences
        sharedPreferences = Mockito.mock<SharedPreferences>()

        // Mock SharedPreferences.Editor
        editor = Mockito.mock()
        `when`(sharedPreferences.edit()).thenReturn(editor)

        // Initialize SharedPreferencesManager with the mocked SharedPreferences
        SharedPreferencesManager.init(sharedPreferences)
    }

    /**
     * Test case for [SharedPreferencesManager.getSelectedLanguage] method.
     */
    @Test
    fun `test getSelectedLanguage returns default language`() {
        // Given
        `when`(sharedPreferences.getString(any(), any())).thenReturn(JAPANESE)

        // When
        val selectedLanguage = SharedPreferencesManager.getSelectedLanguage()

        // Then
        assertEquals(JAPANESE, selectedLanguage)
    }

    /**
     * Test case for [SharedPreferencesManager.updateSelectedLanguage] method.
     */
    @Test
    fun `test updateSelectedLanguage`() {
        // Given
        val jsonString = "ja"

        // When
        SharedPreferencesManager.updateSelectedLanguage(jsonString)

        // Then
        verify(editor).putString(PreferenceKeys.LANGUAGE, jsonString)
        verify(editor).apply()
    }

    /**
     * Test case for [SharedPreferencesManager.getPreferenceBool] method.
     */
    @Test
    fun `test getPreferenceBool`() {
        // Given
        val key = "key"
        val expectedValue = true
        `when`(sharedPreferences.getBoolean(key, false)).thenReturn(expectedValue)

        // When
        val retrievedValue = SharedPreferencesManager.getPreferenceBool(key)

        // Then
        assertEquals(expectedValue, retrievedValue)
    }

    /**
     * Test case for [SharedPreferencesManager.savePreferenceBool] method.
     */
    @Test
    fun `test savePreferenceBool`() {
        // Given
        val key = "key"
        val value = true

        // When
        SharedPreferencesManager.savePreferenceBool(key, value)

        // Then
        verify(editor).putBoolean(key, value)
        verify(editor).apply()
    }
}