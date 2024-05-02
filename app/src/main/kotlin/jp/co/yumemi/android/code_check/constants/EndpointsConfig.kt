package jp.co.yumemi.android.code_check.constants

object EndpointsConfig {

    /**
     * The base URL of the application's API. This URL is used to make network requests.
     */
    const val BASE_URL = "https://api.github.com/search/"

    /**
     * The API header type for content negotiation. This header type specifies the desired format for API responses.
     */
    const val API_HEADER_TYPE = "application/vnd.github.v3+json"

    /** The endpoint for fetching repositories from the GitHub API. */
    const val REPOSITORIES_ENDPOINT = "repositories"
}