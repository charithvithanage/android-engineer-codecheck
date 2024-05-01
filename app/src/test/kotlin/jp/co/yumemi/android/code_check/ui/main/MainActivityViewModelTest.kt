package jp.co.yumemi.android.code_check.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.utils.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for the [MainActivityViewModel] class.
 */
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {
    // Used to ensure that LiveData-related operations are executed synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // The ViewModel under test
    private lateinit var viewModel: MainActivityViewModel

    // Observers for LiveData objects
    @Mock
    private lateinit var isShowHamburgerMenuObserver: Observer<Boolean>

    @Mock
    private lateinit var updateLabelsObserver: Observer<Boolean>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainActivityViewModel()

        // Observe LiveData objects
        viewModel.showHamburgerMenu.observeForever(isShowHamburgerMenuObserver)
        viewModel.updateLabels.observeForever(updateLabelsObserver)
    }

    /**
     * Test case to verify that [MainActivityViewModel.showHamburgerMenu] updates LiveData correctly.
     */
    @Test
    fun `showHamburgerMenu updates LiveData correctly`() {
        viewModel.showHamburgerMenu(true)
        assertThat(viewModel.showHamburgerMenu.getOrAwaitValue(), `is`(true))

        viewModel.showHamburgerMenu(false)
        assertThat(viewModel.showHamburgerMenu.getOrAwaitValue(), `is`(false))
    }

    /**
     * Test case to verify that [MainActivityViewModel.setFragment] updates LiveData value correctly.
     */
    @Test
    fun `setFragment updates LiveData value`() {
        val testName = "TestFragment"
        val expectedFragment = "TestFragment"

        viewModel.setFragment(testName)

        assertEquals(expectedFragment, viewModel.fragment.getOrAwaitValue())
    }

    /**
     * Test case to verify that [MainActivityViewModel.updateLabels] updates LiveData correctly.
     */
    @Test
    fun `setUpdateLabels updates LiveData correctly`() {
        viewModel.setUpdateLabels(true)
        assertThat(viewModel.updateLabels.getOrAwaitValue(), `is`(true))

        viewModel.setUpdateLabels(false)
        assertThat(viewModel.updateLabels.getOrAwaitValue(), `is`(false))
    }

    /**
     * Tests the LiveData update for success message.
     * Verifies if the ViewModel correctly updates the success message LiveData and if it posts the
     * expected success message.
     */
    @Test
    fun `test success message live data updates`() {
        val testSuccessMessage = "Test Success"
        // If your ViewModel can post null success messages, test that scenario
        viewModel.showSuccessDialog(testSuccessMessage)

        // Assert that _successMessage LiveData is updated to null as expected
        viewModel.successMessage.observeForever { newValue ->
            assertEquals(testSuccessMessage, newValue)
        }
    }

    /**
     * Tests the LiveData update for error message.
     * Verifies if the ViewModel correctly updates the error message LiveData and if it posts the
     * expected error message.
     */
    @Test
    fun `test error message live data updates`() {
        val testErrorMessage = "Test Error"
        viewModel.showErrorDialog(testErrorMessage)

        viewModel.errorMessage.observeForever { newValue ->
            assertEquals(testErrorMessage, newValue)
        }
    }

    /**
     * Tests the LiveData update for warning message.
     * Verifies if the ViewModel correctly updates the warning message LiveData and if it posts the
     * expected warning message.
     */
    @Test
    fun `test warn message live data updates`() {
        val testWarnMessage = "Test Warn"
        viewModel.showWarnDialog(testWarnMessage)

        viewModel.warnMessage.observeForever { newValue ->
            assertEquals(testWarnMessage, newValue)
        }
    }

    /**
     *   Test if the `setProgressDialogVisible` method in the viewModel correctly
     *   updates the `isProgressDialogVisible` LiveData.
     */
    @Test
    fun `setProgressDialogVisible updates LiveData correctly`() {
        viewModel.setProgressDialogVisible(true)
        assertThat(viewModel.isProgressDialogVisible.getOrAwaitValue(), `is`(true))

        viewModel.setProgressDialogVisible(false)
        assertThat(viewModel.isProgressDialogVisible.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun `setExitConfirmationDialogVisible updates LiveData correctly`() {
        viewModel.setExitConfirmationDialogVisible(true)
        assertThat(viewModel.existConfirmationDialogVisible.getOrAwaitValue(), `is`(true))

        viewModel.setExitConfirmationDialogVisible(false)
        assertThat(viewModel.existConfirmationDialogVisible.getOrAwaitValue(), `is`(false))
    }


    /**
     * Tear down method to remove observers after testing.
     */
    @After
    fun tearDown() {
        viewModel.showHamburgerMenu.removeObserver(isShowHamburgerMenuObserver)
        viewModel.updateLabels.removeObserver(updateLabelsObserver)
    }
}