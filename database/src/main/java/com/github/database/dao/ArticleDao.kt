package com.github.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.database.models.ArticleCache
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * from articles")
    fun selectAll(): List<ArticleCache>

    @Query("DELETE from articles")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<ArticleCache>)

    @Delete
    suspend fun remove(articles: List<ArticleCache>)
}