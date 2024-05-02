package jp.co.yumemi.android.code_check.ui.favourites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.MockObjects
import jp.co.yumemi.android.code_check.MockObjects.Companion.expectedFavList
import jp.co.yumemi.android.code_check.models.LocalDBQueryResponse
import jp.co.yumemi.android.code_check.models.LocalGitHubRepoObject
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Unit tests for [FavouritesViewModel].
 */
@RunWith(JUnit4::class)
class FavouritesViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: FavouritesViewModel

    @Mock
    private lateinit var savedFavouritesObserver: Observer<List<LocalGitHubRepoObject>>

    @Mock
    private lateinit var localGitHubRepository: LocalGitHubRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = FavouritesViewModel(localGitHubRepository)
        viewModel.allFavourites?.observeForever(savedFavouritesObserver)
    }

    /**
     * Test to verify fetching favourite items from local database.
     */
    @Test
    fun `test get favourite items from local db`() = runBlocking {

        // Create a MutableLiveData and set it as the source for allFavourites LiveData
        val allFavouritesLiveData = MutableLiveData<List<LocalGitHubRepoObject>>()
        allFavouritesLiveData.value = MockObjects.mockFavList

        // Mock the behavior of the repository's getAllRepositories method
        `when`(localGitHubRepository.getAllRepositories()).thenReturn(allFavouritesLiveData)

        val result = localGitHubRepository.getAllRepositories()
        viewModel.allFavourites?.getOrAwaitValue()

        assertEquals(expectedFavList, result?.value)

    }

    /**
     * Test to verify deleting an item from local database using deleteFavourite.
     */
    @Test
    fun `test delete item from local db using deleteFavourite`(): Unit = runBlocking{
        // Mock the behavior of your repository
        `when`(localGitHubRepository.deleteGitHubObjectDao(1)).thenReturn(
            LocalDBQueryResponse(
                true,
                "Deletion successful"
            )
        )

        // Call the function
        viewModel.deleteFavourite(1)

        // Verify that the repository method was called with the correct input
        verify(localGitHubRepository).deleteGitHubObjectDao(1)
    }

    /**
     * Test to verify deleting an item from local database with an unsuccessful deletion.
     */
    @Test
    fun `test delete item from local db with an unsuccessful deletion`(): Unit = runBlocking {
        // Mock the behavior of your repository for an unsuccessful deletion
        `when`(localGitHubRepository.deleteGitHubObjectDao(2)).thenReturn(
            LocalDBQueryResponse(
                false,
                "Deletion failed"
            )
        )

        // Call the function
        viewModel.deleteFavourite(2)

        // Verify that the repository method was called with the correct input
        verify(localGitHubRepository).deleteGitHubObjectDao(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        savedFavouritesObserver.let {
                observer ->
            viewModel.allFavourites?.removeObserver(observer)
        }
        Dispatchers.resetMain()
    }
}