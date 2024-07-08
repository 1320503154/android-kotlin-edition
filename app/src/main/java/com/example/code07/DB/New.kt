package com.example.code07.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class New(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titles: String,
    val authors: String,
    val image: Int
)
