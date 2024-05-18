package com.github.main

import com.github.news.data.Article
import com.github.news.data.ArticleQueryParse
import com.github.news.data.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ForceUpdateUseCase @Inject constructor(
    private val repository: ArticleRepository,
    private val articleQueryParse: ArticleQueryParse
) {
    operator fun invoke(query: String, currentArticles: List<Article>): Flow<State> {
        val articleQuery = articleQueryParse.parse(query)
        return repository.update(articleQuery, currentArticles).map { requestResult ->
            requestResult.toState()
        }
    }
}
