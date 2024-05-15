package com.github.newsapi

import androidx.annotation.IntRange
import com.github.newsapi.models.ArticleCloud
import com.github.newsapi.models.Languages
import com.github.newsapi.models.SortBy
import com.github.newsapi.utils.ApiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.UnknownHostException
import java.util.Date

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
        keyWords: String? = null,
        from: Date? = null,
        to: Date? = null,
        language: Languages? = null,
        sortBy: SortBy? = null,
        @IntRange(from = 0, to = 100)
        pageSize: Int = 100,
        @IntRange(from = 1)
        page: Int = 1,
    ): ArticleResult {
        return try {
            val response = instance.everything(keyWords, from, to, language, sortBy, pageSize, page)
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
