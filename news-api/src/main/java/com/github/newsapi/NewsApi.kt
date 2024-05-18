package com.github.newsapi

import com.github.newsapi.models.ArticleCloud
import com.github.newsapi.models.ArticleQueryCloud
import com.github.newsapi.utils.ApiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.UnknownHostException

class NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null
) {
    private val instance: NewsApiService

    init {
        instance = retrofit(baseUrl, apiKey, okHttpClient).create()
    }

    private fun retrofit(baseUrl: String, apiKey: String, okHttpClient: OkHttpClient?): Retrofit {
        val modifiedOkHttpClient =
            (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
                .addInterceptor(ApiKeyInterceptor(apiKey))
                .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(modifiedOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Suppress("LongParameterList", "SwallowedException")
    suspend fun everything(
        articleQuery: ArticleQueryCloud
    ): ArticleResult {
        return try {
            val response = instance.everything(
                articleQuery.keyWords,
                articleQuery.from,
                articleQuery.to,
                articleQuery.language,
                articleQuery.sortBy,
                articleQuery.pageSize,
                articleQuery.page
            )
            if (response.status == "ok") {
                ArticleResult.Success(response.articles)
            } else {
                ArticleResult.Error(response.message)
            }
        } catch (noInternetException: UnknownHostException) {
            ArticleResult.Error("No internet connection")
        }
    }
}

fun ProvideNewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null
): NewsApi =
    NewsApi(baseUrl, apiKey, okHttpClient)

sealed class ArticleResult {
    data class Success(
        val articles: List<ArticleCloud>
    ) : ArticleResult()

    data class Error(
        val errorMessage: String
    ) : ArticleResult()
}
