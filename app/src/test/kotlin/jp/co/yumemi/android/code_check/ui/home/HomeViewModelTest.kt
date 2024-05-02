package jp.co.yumemi.android.code_check.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.co.yumemi.android.code_check.MockObjects
import jp.co.yumemi.android.code_check.MockObjects.Companion.errorServerResponse
import jp.co.yumemi.android.code_check.repositories.GitHubRepository
import jp.co.yumemi.android.code_check.repositories.LocalGitHubRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the [HomeViewModel] class, which is responsible for testing the functionality
 * of the Home screen's ViewModel.
 *
 * This class uses JUnit4 and Mockito for testing, along with [InstantTaskExecutorRule]
 *
 * @see HomeViewModel
 * @see GitHubRepository
 *
 * @constructor Creates a new instance of [HomeViewModelTest].
 */
@RunWith(JUnit4::class)
class HomeViewModelTest {

    // Rule to execute tasks immediately in the testing environment
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mock GitHub repository for testing
    @Mock
    private lateinit var gitHubRepository: GitHubRepository

    // Mock local GitHub repository for testing
    @Mock
    private lateinit var localGitHubRepository: LocalGitHubRepository

    // ViewModel under test
    private lateinit var viewModel: HomeViewModel

    /**
     * Setup method executed before each test.
     * Initializes the necessary mocks and sets up the [HomeViewModel].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Initialize ViewModel with mocked repositories
        viewModel = HomeViewModel(gitHubRepository,localGitHubRepository)
    }

    /**
     * Test case for successful GitHub repository list retrieval.
     * Verifies that LiveData values are set correctly after calling [HomeViewModel.getGitHubRepoList].
     */
    @Test
    fun `test successful GitHub repository list retrieval`() {
        // Given
        val inputText = "android"

        runBlocking {
            // Mock API response
            Mockito.`when`(gitHubRepository.getRepositories(inputText)).thenReturn(MockObjects.successServerResponseObject)

            // When
            viewModel.getGitHubRepoList(inputText)

            // Then
            // Verify that the LiveData values have been set correctly
            assertEquals(MockObjects.successServerResponseObject.items, viewModel.gitHubRepoList.value)
            Assert.assertNull(viewModel.errorMessage.value)
        }
    }

    /**
     * Test case for failed GitHub repository list retrieval.
     * Verifies that LiveData values are set correctly after calling [HomeViewModel.getGitHubRepoList]
     * with an invalid input.
     */
    @Test
    fun `test failed GitHub repository list retrieval`() {
        val inputText = "invalid_input"

        runBlocking {
            // Mock API response
            Mockito.`when`(gitHubRepository.getRepositories(inputText)).thenReturn(errorServerResponse)

            // When
            viewModel.getGitHubRepoList(inputText)

            // Then
            // Verify that the LiveData values have been set correctly
            Assert.assertNull(viewModel.gitHubRepoList.value)
            assertEquals("Error From Server", viewModel.errorMessage.value)
        }
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