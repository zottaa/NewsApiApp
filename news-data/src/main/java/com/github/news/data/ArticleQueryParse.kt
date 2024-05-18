package com.github.news.data

import jakarta.inject.Inject

interface ArticleQueryParse {
    fun parse(query: String): ArticleQuery

    class Base @Inject constructor() : ArticleQueryParse {
        override fun parse(query: String): ArticleQuery {
            val items = query.split(",")
            return ArticleQuery(
                items.getOrNull(0)
            )
        }
    }
}
