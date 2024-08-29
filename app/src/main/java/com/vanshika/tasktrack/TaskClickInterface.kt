package com.vanshika.tasktrack

interface TaskClickInterface {
    fun updateTask(position : Int)
    fun deleteTask(position: Int)
    fun isCompleted(position: Int,isChecked : Boolean)
}