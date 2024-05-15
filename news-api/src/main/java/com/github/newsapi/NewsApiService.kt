package com.github.newsapi

import androidx.annotation.IntRange
import com.github.newsapi.models.ArticleResponse
import com.github.newsapi.models.Languages
import com.github.newsapi.models.SortBy
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface NewsApiService {
    @Suppress("LongParameterList")
    @GET("everything")
    suspend fun everything(
        @Query("q") keyWords: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") language: Languages? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize")
        @IntRange(from = 0, to = 100)
        pageSize: Int = 100,
        @IntRange(from = 1)
        @Query("page")
        page: Int = 1,
    ): ArticleResponse
}
