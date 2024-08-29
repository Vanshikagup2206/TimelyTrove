package com.vanshika.tasktrack

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@TypeConverters(DateTimeConverter::class)
@Database(entities = [TaskDataClass :: class, CategoryDataClass::class, SubTaskDataClass::class], version = 1, exportSchema = true)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao() : TasksDao
    companion object{
        private var taskDatabase : TasksDatabase ?= null
        fun getInstance(context: Context): TasksDatabase{
            if (taskDatabase == null){
                taskDatabase = Room.databaseBuilder(context,
                TasksDatabase::class.java,
                "TasksDatabase").allowMainThreadQueries()
                    .build()
            }
            return taskDatabase !!
        }
    }
}