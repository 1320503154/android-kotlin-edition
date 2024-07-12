package com.example.code07.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "New")
class New {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "new_titles")
    lateinit var titles: String

    @ColumnInfo(name = "new_authors")
    lateinit var authors: String

    @ColumnInfo(name = "new_image")
    var image: Int = 0

}
