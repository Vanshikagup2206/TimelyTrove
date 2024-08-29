package com.vanshika.tasktrack

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(foreignKeys = [
    ForeignKey(entity = TaskDataClass :: class,
    parentColumns = ["taskId"],
    childColumns = ["taskId"])
])
data class SubTaskDataClass(
    @PrimaryKey(autoGenerate = true)
    var subTaskId : Int = 0,
    var taskId : Int? = 0,
    var subTaskName : String ?= ""
)
