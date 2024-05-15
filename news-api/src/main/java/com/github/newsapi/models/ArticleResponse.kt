package com.github.newsapi.models

import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleCloud>,
    val code: String,
    val message: String
)
