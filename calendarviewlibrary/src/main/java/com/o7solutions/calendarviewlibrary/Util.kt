package com.o7solutions.calendarviewlibrary

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager

object Util {

    //Int to DP
    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    var months = arrayListOf<String>().apply {
        add("Jan")
        add("Feb")
        add("Mar")
        add("Apr")
        add("May")
        add("Jun")
        add("Jul")
        add("Aug")
        add("Sep")
        add("Oct")
        add("Nov")
        add("Dec")
    }

    var weekDays = arrayListOf<String>().apply {
        add("Sun")
        add("Mon")
        add("Tues")
        add("Wed")
        add("Thurs")
        add("Fri")
        add("Sat")
    }
}