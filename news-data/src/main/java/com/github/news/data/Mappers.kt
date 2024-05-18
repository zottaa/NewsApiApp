package com.github.news.data

import com.github.database.models.ArticleCache
import com.github.database.models.SourceCache
import com.github.newsapi.ArticleResult
import com.github.newsapi.models.ArticleCloud
import com.github.newsapi.models.ArticleQueryCloud
import com.github.newsapi.models.SourceCloud

internal fun ArticleCache.toArticle(): Article =
    with(this) {
        Article(
            source.toSource(),
            author,
            title,
            url,
            urlToImage,
            publishedAt,
            content
        )
    }

internal fun SourceCache.toSource(): Source =
    with(this) {
        Source(
            id,
            name
        )
    }

internal fun ArticleCloud.toArticle(): Article =
    with(this) {
        Article(
            source.toSource(),
            author,
            title,
            url,
            urlToImage,
            publishedAt,
            content
        )
    }

internal fun SourceCloud.toSource(): Source =
    with(this) {
        Source(
            id ?: "",
            name
        )
    }

internal fun Article.toArticleCache(): ArticleCache =
    with(this) {
        ArticleCache(
            id = 0,
            source = source.toSourceCache(),
            author = author,
            title = title,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )
    }

internal fun Source.toSourceCache(): SourceCache =
    with(this) {
        SourceCache(
            id,
            name
        )
    }

internal fun ArticleResult.toRequestResult(): RequestResult<List<Article>> {
    return when (this) {
        is ArticleResult.Success -> {
            RequestResult.Success(
                this.articles.map {
                    it.toArticle()
                }.filter {
                    it.title != "[Removed]"
                }
            )
        }

        else -> {
            RequestResult.Error(
                null,
                (this as ArticleResult.Error).errorMessage
            )
        }
    }
}

internal fun ArticleQuery.toArticleQueryCloud(): ArticleQueryCloud {
    return with(this) {
        ArticleQueryCloud(
            keyWords,
            from,
            to,
            language,
            sortBy,
            pageSize,
            page
        )
    }
}
