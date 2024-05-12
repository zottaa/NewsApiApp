package com.github.news_main

import com.github.news.data.Article

sealed class State {

    object Initial : State()
    data class Loading(
        private val articles: List<Article>
    ) : State()
    data class Error(
        private val articles: List<Article>,
        private val errorMessage: String
    ) : State()

    data class Success(
        private val articles: List<Article>
    ) : State()
}