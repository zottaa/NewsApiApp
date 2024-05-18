package com.github.main

import com.github.news.data.Article
import com.github.news.data.RequestResult

internal fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Success -> State.Success(this.data ?: emptyList())
        is RequestResult.InProgress -> State.Loading(this.data ?: emptyList())
        is RequestResult.Error -> State.Error(this.data ?: emptyList(), this.errorMessage)
    }
}
