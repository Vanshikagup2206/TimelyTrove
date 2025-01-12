package com.o7solutions.calendarviewlibrary

import android.view.View
import java.util.*
import kotlin.collections.ArrayList

interface CalenderViewInterface {
    fun builder(): CalendarView
    fun withEvents(events: ArrayList<Calendar>? = null,eventDotColor: Int? = null): CalendarView
    fun withYearPanel(dateFormat: String? = null,textColor: Int? = null,textSize: Float? = null,font: Int? = null): CalendarView
    fun withYearPanleMargin(top: Int = 0,bottom: Int = 0,left: Int = 0,right: Int = 0): CalendarView
    fun withMonthPanel(font: Int? = null, textSize: Float? = null, selectedTextColor: Int? = null, unSelectedTextColor:Int? = null, background: Int? = null,months: ArrayList<String>? = null): CalendarView
    fun withMonthPanleMargin(top: Int = 0,bottom: Int = 0,left: Int = 0,right: Int = 0): CalendarView
    fun withWeekPanel(font: Int? = null, textColor: Int? = null, textSize: Float? = null, background: Int? = null,weekDays: ArrayList<String>? = null): CalendarView
    fun withWeekPanelMargin(top: Int = 0,bottom: Int = 0,left: Int = 0,right: Int = 0): CalendarView
    fun withDayPanel(font: Int? = null, textColor: Int? = null, textSize: Float? = null, selectedTextColor: Int? = null, selectedBackground:Int? = null, background: Int? = null): CalendarView
    fun withDayPanelMargin(top: Int = 0,bottom: Int = 0,left: Int = 0,right: Int = 0): CalendarView
    fun withCalenderViewBg(background: Int? = null): CalendarView
    fun withUpdateSelectDate(selectedDate: Calendar): CalendarView
    fun buildCalendar(): CalendarView
    fun onMonthClick(view: View?, month: String, position: Int)
    interface EventHandler{
        fun onDayClick(view: View?, date: Date, position: Int)
        fun onDayLongClick(view: View?, date: Date, position: Int)
        fun onMonthClick(view: View?, month: String, position: Int)
    }
}