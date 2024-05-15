package com.github.main

import com.github.news.data.Article

sealed class State(val articles: List<Article>) {

    object Initial : State(emptyList())
    class Loading(articles: List<Article>) : State(articles)

    class Error(
        articles: List<Article>,
        val errorMessage: String
    ) : State(articles)

    class Success(
        articles: List<Article>
    ) : State(articles)
}
