package com.github.zottaa

import android.content.Context
import com.github.database.ProvideNewsDatabase
import com.github.news.data.ArticleQueryParse
import com.github.news.data.ArticleRepository
import com.github.newsapi.ProvideNewsApi
import com.github.utility.Dispatcher
import com.github.utility.DispatcherType
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindArticleRepository(repository: ArticleRepository.Base): ArticleRepository

    @Binds
    abstract fun bindArticleQueryParse(articleQueryParse: ArticleQueryParse.Base): ArticleQueryParse

    companion object {
        @Provides
        @Singleton
        fun providesNewsDataBase(@ApplicationContext context: Context) =
            ProvideNewsDatabase(context)

        @Provides
        @Singleton
        fun providesNewsApi() =
            ProvideNewsApi(
                baseUrl = BuildConfig.NEWS_API_BASE_URL,
                apiKey = BuildConfig.NEWS_API_KEY,
            )

        @Provides
        @Dispatcher(DispatcherType.IO)
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Dispatcher(DispatcherType.Main)
        fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }
}
