package com.github.newsapi.models

import androidx.annotation.IntRange
import java.util.Date

data class ArticleQueryCloud(
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
