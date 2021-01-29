package com.java90.ktornotesapp.data.remote

import com.java90.ktornotesapp.utils.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor is for make the changes of @HEADERS on each request
 * that needs authentication.
 *
 * Authorization is just for make the request
 * Authentication is for be able to change data in our Database.
 */

class BasicAuthInterceptor : Interceptor {

    val email: String? = null
    val password: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath in IGNORE_AUTH_URLS) return chain.proceed(request)
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", Credentials.basic(email ?: "", password ?: ""))
            .build()
        return chain.proceed(authenticatedRequest)
    }
}