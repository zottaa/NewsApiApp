package com.github.news.data

import com.github.database.NewsDataBase
import com.github.newsapi.NewsApi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

interface ArticleRepository {
    fun all(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy()
    ): Flow<RequestResult<List<Article>>>

    class Base @Inject constructor(
        private val db: NewsDataBase,
        private val api: NewsApi
    ) : ArticleRepository {
        override fun all(
            query: String,
            mergeStrategy: MergeStrategy<RequestResult<List<Article>>>
        ): Flow<RequestResult<List<Article>>> {
            val dbRequestStartFlow = flow<RequestResult<List<Article>>> {
                emit(RequestResult.InProgress(null))
            }
            val dbRequestFlow = flow<RequestResult<List<Article>>> {
                val articles = db.articleDao.selectAll().map { articleCache ->
                    articleCache.toArticle()
                }
                emit(RequestResult.Success(articles))
            }
            val cache = merge(dbRequestStartFlow, dbRequestFlow)

            val serverRequestStartFlow = flow<RequestResult<List<Article>>> {
                emit(RequestResult.InProgress(null))
            }
            val serverRequestFlow = flow {
                val apiResponse = api.everything(query)
                val requestResult = apiResponse.toRequestResult()
                emit(apiResponse.toRequestResult())
                if (requestResult.date != null) {
                    db.articleDao.clear()
                    db.articleDao.insert(requestResult.date.map { it.toArticleCache() })
                }
            }
            val server = merge(serverRequestStartFlow, serverRequestFlow)

            return cache.combine(server, mergeStrategy::merge)
        }
    }
}

sealed class RequestResult<E>(
    val date: E?
) {
    class InProgress<E>(date: E?) : RequestResult<E>(date)
    class Success<E>(date: E?) : RequestResult<E>(date)
    class Error<E>(date: E?, val errorMessage: String) : RequestResult<E>(date)
}
