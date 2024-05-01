package jp.co.yumemi.android.code_check.ui.repodetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.MockObjects.Companion.expectedGitHubRepoObject
import jp.co.yumemi.android.code_check.MockObjects.Companion.mockGitHubRepoObject
import jp.co.yumemi.android.code_check.models.GitHubRepoObject
import jp.co.yumemi.android.code_check.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Unit tests for [RepoDetailsViewModel].
 *
 * This class contains unit tests for the functionality of the [RepoDetailsViewModel] class.
 */
@RunWith(JUnit4::class)
class RepoDetailsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RepoDetailsViewModel

    @Mock
    private lateinit var gitRepoDataObserver: Observer<GitHubRepoObject>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = RepoDetailsViewModel()
        viewModel.gitRepoData.observeForever(gitRepoDataObserver)
    }

    /**
     * Unit test for [RepoDetailsViewModel.setGitRepoData].
     *
     * This test verifies that setting a value to the live data using [RepoDetailsViewModel.setGitRepoData]
     * correctly updates the LiveData value.
     */
    @Test
    fun `test set value to the live data using setGitRepoData`() {

        // When setting the GitHubRepoObject in the ViewModel
        viewModel.setGitRepoData(mockGitHubRepoObject)

        // Then the LiveData value should be updated
        val result = viewModel.gitRepoData.getOrAwaitValue()
        assertEquals(expectedGitHubRepoObject, result)
    }

    /**
     * Tear-down method executed after each test.
     * Resets the main dispatcher.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}