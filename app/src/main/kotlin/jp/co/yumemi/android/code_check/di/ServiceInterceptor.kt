package jp.co.yumemi.android.code_check.di

import jp.co.yumemi.android.code_check.constants.EndpointsConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * An OkHttp Interceptor responsible for modifying network requests and headers before they are sent.
 * It adds custom headers, such as "Accept", to the outgoing requests.
 */
class ServiceInterceptor : Interceptor {

    /**
     * Intercepts the network request, modifies headers, and proceeds with the request.
     *
     * @param chain The OkHttp Interceptor Chain for handling the request.
     * @return The response received after processing the modified request.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        /*
         * Modify network requests and headers
         */
        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("Accept", EndpointsConfig.API_HEADER_TYPE)
            .build()
        return chain.proceed(modifiedRequest)
    }

}
