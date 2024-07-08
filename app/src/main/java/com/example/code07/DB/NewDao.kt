package com.example.code07.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewDao {
    @Query("select * from New")
    fun getAll():List<New>
    @Insert
    fun insertNew(newItem: New)
}
