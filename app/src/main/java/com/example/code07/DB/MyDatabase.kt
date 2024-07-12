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
            // 创建一个Room数据库的单例
            Room.databaseBuilder(context,MyDatabase::class.java,name).enableMultiInstanceInvalidation()
                .build().apply {
                    db=this
                }
        } else {
            db!!
        }
        // 清除数据库中的所有数据
        fun clearData() {
            db?.clearAllTables()
        }
    }

}

