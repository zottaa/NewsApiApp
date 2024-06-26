package com.github.main

import com.github.news.data.ArticleQueryParse
import com.github.news.data.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllArticleUseCase @Inject constructor(
    private val repository: ArticleRepository,
    private val articleQueryParse: ArticleQueryParse
) {
    operator fun invoke(query: String): Flow<State> {
        val articleQuery = articleQueryParse.parse(query)
        return repository.all(articleQuery).map { requestResult ->
            requestResult.toState()
        }
    }
}
