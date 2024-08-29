package com.vanshika.tasktrack

import androidx.room.Embedded
import androidx.room.Relation

data class TaskShownList(
    @Embedded
    var categoryDataClass: CategoryDataClass,

    @Relation(entity = TaskDataClass::class,
    parentColumn = "categoryId",
    entityColumn = "categoryId")
    var taskList: List<TaskDataClass>
)
