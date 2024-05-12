package com.github.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.database.utils.DateConverter
import java.util.Date

@Entity(tableName = "articles")
data class ArticleCache(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @Embedded(prefix = "source.")
    val source: SourceCache,
    @ColumnInfo("author")
    val author: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("urlToImage")
    val urlToImage: String,
    @ColumnInfo("publishedAt")
    val publishedAt: Date,
    @ColumnInfo("content")
    val content: String
)