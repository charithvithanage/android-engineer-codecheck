package jp.co.yumemi.android.code_check.ui.repodetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.MockObjects.Companion.expectedGitHubRepoObject
import jp.co.yumemi.android.code_check.MockObjects.Companion.mockGitHubRepoObject
import jp.co.yumemi.android.code_check.models.GitHubRepoObject
import jp.co.yumemi.android.code_check.models.LocalDBQueryResponse
import jp.co.yumemi.android.code_check.models.toGitHubDataClass
import jp.co.yumemi.android.code_check.repositories.LocalGitHubRepository
import jp.co.yumemi.android.code_check.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mockito
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

    @Mock
    private lateinit var localGitHubRepository: LocalGitHubRepository

    @Mock
    private lateinit var localDBResponseObserver: Observer<LocalDBQueryResponse>

    @Mock
    private lateinit var favouriteStatusObserver: Observer<Boolean>
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = RepoDetailsViewModel(localGitHubRepository)
        viewModel.localDBResponse.observeForever(localDBResponseObserver)
        viewModel.favouriteStatus.observeForever(favouriteStatusObserver)
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
     * Test case for addToFavourites()
     */
    @Test
    fun `test add item to local db using addToFavourites`() = runBlocking {

        // When setting the GitHubRepoObject in the ViewModel
        viewModel.setGitRepoData(mockGitHubRepoObject)

        // Then the LiveData value should be updated
        val result = viewModel.gitRepoData.getOrAwaitValue()

        val mockLocalGitHubRepoObject = result.toGitHubDataClass()

        // Mock the behavior of your repository
        Mockito.`when`(localGitHubRepository.insertGitHubObject(mockLocalGitHubRepoObject)).thenReturn(
            LocalDBQueryResponse(
                true,
                "Added Success"
            )
        ) // Adjust based on your data classes and response structure.

        // Call the function
        viewModel.addToFavourites()

        viewModel.localDBResponse.getOrAwaitValue()
        Mockito.verify(localDBResponseObserver).onChanged(
            LocalDBQueryResponse(
                true,
                "Added Success"
            )
        )
        assertEquals(true, viewModel.favouriteStatus.value)
    }

    @Test
    fun `test delete item from local db using deleteFavourite`() = runBlocking {

        // Mock the behavior of your repository
        Mockito.`when`(localGitHubRepository.deleteGitHubObjectDao(1)).thenReturn(
            LocalDBQueryResponse(
                true,
                "Delete Success"

            )
        ) // Adjust based on your data classes and response structure.

        // Call the function
        viewModel.deleteFavourite(1)

        viewModel.localDBResponse.getOrAwaitValue()
        Mockito.verify(localDBResponseObserver).onChanged(
            LocalDBQueryResponse(
                true,
                "Delete Success"
            )
        )
        assertEquals(false, viewModel.favouriteStatus.value)
    }

    /**
     * Test case for setGitRepoData()
     */
    @Test
    fun `test set value to the favouriteStatus live data using checkFavStatus`() {
        viewModel.checkFavStatus(true)
        val result = viewModel.favouriteStatus.getOrAwaitValue()
        assertEquals(true, result)
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