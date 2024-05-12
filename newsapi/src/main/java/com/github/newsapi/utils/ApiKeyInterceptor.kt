package com.github.newsapi.utils

import okhttp3.Interceptor

internal class ApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader(
                    API_KEY_HEADER,
                    apiKey
                )
                .build()
        )

    companion object {
        private const val API_KEY_HEADER = "X-Api-Key"
    }
}