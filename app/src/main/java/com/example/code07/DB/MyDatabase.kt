package com.example.code07.DB

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [New::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getNewDao(): NewDao
    companion object{
        private var db:MyDatabase?=null
        private val name="myapp"
        fun getDB(context: Context)=if (db==null){
            Room.databaseBuilder(context,MyDatabase::class.java,name).enableMultiInstanceInvalidation()
                .build().apply {
                    db=this
                }
        } else {
            db!!
        }
        fun clearData() {
            db?.clearAllTables()
        }
    }

}

