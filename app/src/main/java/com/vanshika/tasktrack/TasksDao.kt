package com.vanshika.tasktrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TasksDao {
    @Insert
    fun insertCategory(categoryDataClass: CategoryDataClass)

    @Query("SELECT * FROM CategoryDataClass")
    fun getCategory() : List<CategoryDataClass>

    @Query("SELECT * FROM TaskDataClass WHERE categoryId = :categoryId")
    fun getTaskAccCat(categoryId: Int) : List<TaskDataClass>

    @Update
    fun updateCategory(categoryDataClass: CategoryDataClass)

    @Delete
    fun deleteCategory(categoryDataClass: CategoryDataClass)

    @Query("SELECT * FROM TaskDataClass WHERE taskId =:taskId")
    fun taskAccId(taskId: Int) : TaskDataClass

    @Insert
    fun insertTask(taskDataClass: TaskDataClass)

    @Query("SELECT * FROM TaskDataClass")
    fun getAllTask() : List<TaskDataClass>

    @Query("SELECT * FROM TaskDataClass WHERE taskId =:taskId")
    fun getTaskData(taskId : Int) : List<TaskDataClass>

    @Update
    fun updateTask(taskDataClass: TaskDataClass)

    @Delete
    fun deleteTask(taskDataClass: TaskDataClass)

    @Query("Select * From TaskDataClass Where taskId = :taskId")
    fun getSingleTaskById(taskId: Int):TaskDataClass

    @Insert
    fun insertSubTask(subTaskDataClass: SubTaskDataClass)

    @Query("SELECT * FROM SubTaskDataClass WHERE subTaskId =:subTaskId")
    fun getSubTask(subTaskId : Int) : List<SubTaskDataClass>

    @Update
    fun updateSubTask(subTaskDataClass: SubTaskDataClass)

    @Delete
    fun deleteSubTask(subTaskDataClass: SubTaskDataClass)


    @Query("SELECT * FROM TaskDataClass WHERE taskStartTime>=:startDate and taskEndTime<=:endDate")
    fun getTaskBetweenDates(startDate: Long, endDate: Long) : List<TaskDataClass>
}