package com.vanshika.tasktrack

import android.app.ActivityManager.TaskDescription
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Calendar
import java.util.Date

@Entity
data class TaskDataClass(
    @PrimaryKey(autoGenerate = true)
    var taskId : Int = 0,
    var taskName : String ?="",
    var taskDescription: String ?= "",
    var taskSubTask : String ?= "",
    var taskDate : Date ?= Calendar.getInstance().time,
    var taskStartTime : Date ?= Calendar.getInstance().time,
    var taskEndTime : Date ?=Calendar.getInstance().time,
    var categoryId : Int? = 0,
    var categoryName : String ?= "",
    var color : Int ?= 0,
    var isCompleted : Boolean ?=false
){
    override fun toString(): String {
        return "$taskName"
    }
}
