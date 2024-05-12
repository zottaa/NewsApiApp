package com.github.newsapi.models

import com.github.zottaa.utils.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ArticleCloud(
    @SerialName("source")
    val source: SourceCloud,
    @SerialName("author")
    val author: String,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String,
    @SerialName("urlToImage")
    val urlToImage: String,
    @SerialName("publishedAt")
    @Serializable(with = DateSerializer::class)
    val publishedAt: Date,
    @SerialName("content")
    val content: String
)
