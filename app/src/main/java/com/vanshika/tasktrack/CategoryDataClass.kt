package com.vanshika.tasktrack

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class CategoryDataClass(
    @PrimaryKey(autoGenerate = true)
    var categoryId : Int = 0,
    var categoryName : String ?= ""
){
    override fun toString(): String {
        return "$categoryName"
    }
}
