package com.github.news.data

import com.github.database.NewsDataBase
import com.github.newsapi.NewsApi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge

interface ArticleRepository {
    fun all(
        query: ArticleQuery,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy()
    ): Flow<RequestResult<List<Article>>>

    fun update(
        query: ArticleQuery,
        currentArticles: List<Article>,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy(),
    ): Flow<RequestResult<List<Article>>>

    class Base @Inject constructor(
        private val db: NewsDataBase,
        private val api: NewsApi
    ) : ArticleRepository {
        override fun all(
            query: ArticleQuery,
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
                val apiResponse = api.everything(query.toArticleQueryCloud())
                val requestResult = apiResponse.toRequestResult()
                emit(requestResult)
                if (requestResult.data != null) {
                    db.articleDao.clear()
                    db.articleDao.insert(requestResult.data.map { it.toArticleCache() })
                }
            }
            val server = merge(serverRequestStartFlow, serverRequestFlow)

            return cache.combine(server, mergeStrategy::merge)
        }

        override fun update(
            query: ArticleQuery,
            currentArticles: List<Article>,
            mergeStrategy: MergeStrategy<RequestResult<List<Article>>>,
        ): Flow<RequestResult<List<Article>>> {
            val serverRequestStartFlow = flow<RequestResult<List<Article>>> {
                emit(RequestResult.InProgress(currentArticles))
            }
            val serverRequestFlow = flow {
                val apiResponse = api.everything(query.toArticleQueryCloud())
                val requestResult = apiResponse.toRequestResult()
                emit(requestResult)
                if (requestResult.data != null) {
                    db.articleDao.clear()
                    db.articleDao.insert(requestResult.data.map { it.toArticleCache() })
                }
            }
            val server = merge(serverRequestStartFlow, serverRequestFlow)
            return flowOf(RequestResult.Success(currentArticles)).combine(
                server,
                mergeStrategy::merge
            )
        }
    }
}

sealed class RequestResult<E>(
    val data: E?
) {
    class InProgress<E>(data: E?) : RequestResult<E>(data)
    class Success<E>(data: E?) : RequestResult<E>(data)
    class Error<E>(data: E?, val errorMessage: String) : RequestResult<E>(data)
}
