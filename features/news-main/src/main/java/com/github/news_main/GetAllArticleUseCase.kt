package com.github.news_main

import com.github.news.data.Article
import com.github.news.data.ArticleRepository
import com.github.news.data.MergeStrategy
import com.github.news.data.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllArticleUseCase @Inject constructor(
    private val repository: ArticleRepository,
    private val mergeStrategy: MergeStrategy<RequestResult<List<Article>>>
) {
    operator fun invoke(): Flow<State> {
        return repository.all(mergeStrategy)
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