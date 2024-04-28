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
     * Tear down method to remove observers after testing.
     */
    @After
    fun tearDown() {
        viewModel.showHamburgerMenu.removeObserver(isShowHamburgerMenuObserver)
        viewModel.updateLabels.removeObserver(updateLabelsObserver)
    }
}