package com.github.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "source")
data class SourceCache(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String
)