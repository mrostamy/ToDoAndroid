package com.example.todoapp.utils.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.utils.room.dao.TaskDao
import com.example.todoapp.utils.room.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = true)
abstract class RoomDB : RoomDatabase() {


    companion object {

        @Volatile
        private var instance: RoomDB? = null

        fun getInstance(context: Context): RoomDB {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context, RoomDB::
                        class.java, "tasksDB"
                    ).build()
                }
            }
            return instance!!
        }
    }

    abstract fun getTaskDao(): TaskDao

}