package jp.co.yumemi.android.code_check.repositories

import jp.co.yumemi.android.code_check.MockObjects.Companion.errorServerResponse
import jp.co.yumemi.android.code_check.MockObjects.Companion.successServerResponseObject
import jp.co.yumemi.android.code_check.apiservices.GitRepoApiService
import jp.co.yumemi.android.code_check.models.ApiResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/**
 * Unit tests for UserRepositoryImpl class.
 */
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {

    @Mock
    private lateinit var gitRepoApiService: GitRepoApiService

    @Mock
    private lateinit var gitHubRepositoryImpl: GitHubRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        gitRepoApiService = mock(GitRepoApiService::class.java)
    }

    /**
     * Test case to verify successful response with GitHub data list.
     */
    @Test
    fun `test getRepositories returns success response with git hub data list`() {
        runBlocking {
            val searchText = "charithvin"
            Mockito.`when`(gitHubRepositoryImpl.getRepositories(searchText)).thenReturn(
                successServerResponseObject
            )
            // When
            val result = gitHubRepositoryImpl.getRepositories(searchText)

            // Then
            Assert.assertNotNull(result)
            Assert.assertEquals(result.message, "Data fetched Successfully")
            Assert.assertEquals(result.items?.size, 1)
            Assert.assertTrue(result.success)
        }
    }

    /**
     * Test case to verify successful response without GitHub data list.
     */
    @Test
    fun `test getRepositories returns success response without git hub data list`() {
        runBlocking {
            val successServerResponseObject =
                ApiResponse(
                    success = true,
                    message = "Data fetched Successfully",
                    items = listOf()
                )

            val searchText = "charithvin"
            Mockito.`when`(gitHubRepositoryImpl.getRepositories(searchText)).thenReturn(
                successServerResponseObject
            )
            // When
            val result = gitHubRepositoryImpl.getRepositories(searchText)

            // Then
            Assert.assertNotNull(result)
            Assert.assertTrue(result.success)
            Assert.assertEquals(result.items?.size, 0)
        }
    }

    /**
     * Test case to verify response with server error.
     */
    @Test
    fun `test getRepositories returns success response with server error`() {
        val searchText = "charithvin"

        runBlocking {
            Mockito.`when`(gitHubRepositoryImpl.getRepositories(searchText)).thenReturn(
                errorServerResponse
            )

            // When
            val result = gitHubRepositoryImpl.getRepositories(searchText)
            // Then
            Assert.assertNotNull(result)
            Assert.assertEquals(result.message, "Error From Server")
            Assert.assertFalse(result.success)
        }
    }

    /**
     * Test case to verify successful response from remote service.
     */
    @Test
    fun `test getRepositoriesFromRemoteService success response`() {
        val searchText = "charithvin"

        runBlocking {
            Mockito.`when`(gitRepoApiService.getRepositories(searchText))
                .thenReturn(Response.success(successServerResponseObject))
            // When
            val response = gitRepoApiService.getRepositories(searchText)
            // Then
            Assert.assertNotNull(response)
            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(200, (response.code()))
        }
    }
}
