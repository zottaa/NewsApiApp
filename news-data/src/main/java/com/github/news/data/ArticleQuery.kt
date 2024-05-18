package com.github.news.data

import androidx.annotation.IntRange
import com.github.newsapi.models.Languages
import com.github.newsapi.models.SortBy
import java.util.Date

data class ArticleQuery(
    val keyWords: String? = null,
    val from: Date? = null,
    val to: Date? = null,
    val language: Languages? = null,
    val sortBy: SortBy? = null,
    @IntRange(from = 0, to = 100)
    val pageSize: Int = 100,
    @IntRange(from = 1)
    val page: Int = 1,
)
