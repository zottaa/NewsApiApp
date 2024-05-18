@file:Suppress("UNUSED_PARAMETER")

package com.github.news.data

interface MergeStrategy<E> {
    fun merge(left: E, right: E): E
}

class RequestResultMergeStrategy : MergeStrategy<RequestResult<List<Article>>> {
    @Suppress("CyclomaticComplexMethod")
    override fun merge(
        left: RequestResult<List<Article>>,
        right: RequestResult<List<Article>>
    ): RequestResult<List<Article>> {
        return when {
            left is RequestResult.Success && right is RequestResult.Success -> merge(
                left,
                right
            )

            left is RequestResult.Success && right is RequestResult.InProgress -> merge(
                left,
                right
            )

            left is RequestResult.Success && right is RequestResult.Error -> merge(
                left,
                right
            )

            left is RequestResult.InProgress && right is RequestResult.Success -> merge(
                left,
                right
            )

            left is RequestResult.InProgress && right is RequestResult.InProgress -> merge(
                left,
                right
            )

            left is RequestResult.InProgress && right is RequestResult.Error -> merge(
                left,
                right
            )

            left is RequestResult.Error -> merge(
                left,
                right
            )

            else -> error("Unimplemented branch")
        }
    }

    private fun merge(
        cache: RequestResult.Success<List<Article>>,
        server: RequestResult.Success<List<Article>>
    ): RequestResult<List<Article>> {
        return RequestResult.Success(server.data)
    }

    private fun merge(
        cache: RequestResult.Success<List<Article>>,
        server: RequestResult.InProgress<List<Article>>
    ): RequestResult<List<Article>> {
        return RequestResult.InProgress(cache.data)
    }

    private fun merge(
        cache: RequestResult.Success<List<Article>>,
        server: RequestResult.Error<List<Article>>
    ): RequestResult<List<Article>> {
        return RequestResult.Error(cache.data, server.errorMessage)
    }

    private fun merge(
        cache: RequestResult.InProgress<List<Article>>,
        server: RequestResult.Success<List<Article>>
    ): RequestResult<List<Article>> {
        return RequestResult.Success(server.data)
    }

    private fun merge(
        cache: RequestResult.InProgress<List<Article>>,
        server: RequestResult.InProgress<List<Article>>
    ): RequestResult<List<Article>> {
        return when {
            cache.data != null -> RequestResult.InProgress(cache.data)
            server.data != null -> RequestResult.InProgress(server.data)
            else -> RequestResult.InProgress(null)
        }
    }

    private fun merge(
        cache: RequestResult.InProgress<List<Article>>,
        server: RequestResult.Error<List<Article>>
    ): RequestResult<List<Article>> {
        return when {
            cache.data != null -> RequestResult.Error(cache.data, server.errorMessage)
            server.data != null -> RequestResult.Error(server.data, server.errorMessage)
            else -> RequestResult.Error(null, server.errorMessage)
        }
    }

    private fun merge(
        cache: RequestResult.Error<List<Article>>,
        server: RequestResult.Success<List<Article>>
    ): RequestResult<List<Article>> {
        return RequestResult.Error(server.data, cache.errorMessage)
    }

    private fun merge(
        cache: RequestResult.Error<List<Article>>,
        server: RequestResult.InProgress<List<Article>>
    ): RequestResult<List<Article>> {
        return when {
            cache.data != null -> RequestResult.Error(cache.data, cache.errorMessage)
            server.data != null -> RequestResult.Error(server.data, cache.errorMessage)
            else -> RequestResult.Error(null, cache.errorMessage)
        }
    }

    private fun merge(
        cache: RequestResult.Error<List<Article>>,
        server: RequestResult.Error<List<Article>>
    ): RequestResult<List<Article>> {
        return when {
            cache.data != null -> RequestResult.Error(
                cache.data,
                "server: ${server.errorMessage} cache: ${cache.errorMessage}"
            )

            server.data != null -> RequestResult.Error(
                server.data,
                "server: ${server.errorMessage} cache: ${cache.errorMessage}"
            )

            else -> RequestResult.Error(
                null,
                "server: ${server.errorMessage} cache: ${cache.errorMessage}"
            )
        }
    }
}
