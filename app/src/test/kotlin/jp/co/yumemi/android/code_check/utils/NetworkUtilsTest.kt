package jp.co.yumemi.android.code_check.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetworkUtilsTest {

    @Mock
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Before
    fun setup() {
        mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)

        // Inject the mock ConnectivityManager before each test
        NetworkUtils.init(mockConnectivityManager)
    }

    @Test
    fun `test isNetworkAvailable with network available`() {
        // Mock a connected network with cellular capabilities
        val mockNetwork = Mockito.mock(Network::class.java)
        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.activeNetwork).thenReturn(mockNetwork)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockNetwork)).thenReturn(mockNetworkCapabilities)
        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            .thenReturn(true)

        Assert.assertTrue(NetworkUtils.isNetworkAvailable())
    }

    @Test
    fun `test isNetworkAvailable with no network available`() {
        // Mock no active network
        Mockito.`when`(mockConnectivityManager.activeNetwork).thenReturn(null)

        Assert.assertFalse(NetworkUtils.isNetworkAvailable())
    }
}