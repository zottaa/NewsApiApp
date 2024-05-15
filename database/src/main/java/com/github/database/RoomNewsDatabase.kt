package com.github.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.database.dao.ArticleDao
import com.github.database.models.ArticleCache
import com.github.database.models.SourceCache
import com.github.database.utils.DateConverter

@Database(
    entities = [ArticleCache::class, SourceCache::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class RoomNewsDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}

class NewsDataBase internal constructor(
    private val db: RoomNewsDatabase
) {
    val articleDao: ArticleDao
        get() = db.articleDao()
}

fun ProvideNewsDatabase(applicationContext: Context): NewsDataBase {
    val roomNewsDatabase = Room.databaseBuilder(
        applicationContext.applicationContext,
        RoomNewsDatabase::class.java,
        "news"
    )
        .build()
    return NewsDataBase(roomNewsDatabase)
}
