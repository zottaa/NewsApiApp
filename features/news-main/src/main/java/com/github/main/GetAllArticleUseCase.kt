package com.github.main

import com.github.news.data.Article
import com.github.news.data.ArticleRepository
import com.github.news.data.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllArticleUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {
    operator fun invoke(query: String): Flow<State> {
        return repository.all(query)
            .map { it.toState() }
    }
}

internal fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Success -> State.Success(this.date ?: emptyList())
        is RequestResult.InProgress -> State.Loading(this.date ?: emptyList())
        is RequestResult.Error -> State.Error(this.date ?: emptyList(), this.errorMessage)
    }
}
