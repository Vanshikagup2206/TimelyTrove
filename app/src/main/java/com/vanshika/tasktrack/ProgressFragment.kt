package com.vanshika.tasktrack

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.vanshika.tasktrack.databinding.FragmentProgressBinding
import com.vanshika.tasktrack.databinding.MonthPickerBinding
import com.vanshika.tasktrack.databinding.RangePickerDialogBinding
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProgressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentProgressBinding? = null
    var array = arrayListOf<String>()
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    var tasksDatabase: TasksDatabase? = null
    var taskList = arrayListOf<TaskDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProgressBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        array.addAll(resources.getStringArray(R.array.month_picker))
        binding?.btnDay?.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { _, year, month, date ->
                    startCalendar.set(year, month, date, 0, 0, 0)
                    endCalendar.set(year, month, date, 23, 59, 59)
                    getData()
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }

        binding?.btnWeekly?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialog = RangePickerDialogBinding.inflate(layoutInflater)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setContentView(dialog.root)
                show()
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.calendar.setCalendarListener(object : CalendarListener {
                    override fun onFirstDateSelected(startDate: Calendar) {
                        startCalendar = startDate
                    }

                    override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                        startCalendar = startDate
                        val differenceInMillis = endDate.timeInMillis - startDate.timeInMillis
                        val maxDaysInMillis = 7 * 24 * 60 * 60 * 1000L
                        if (differenceInMillis > maxDaysInMillis) {
                            Toast.makeText(
                                context,
                                R.string.select_seven_days_only,
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        endCalendar = endDate

                    }
                })
                dialog.btnOk.setOnClickListener {
                    val differenceInMillis = endCalendar.timeInMillis - startCalendar.timeInMillis
                    val maxDaysInMillis = 7 * 24 * 60 * 60 * 1000L
                    if (differenceInMillis > maxDaysInMillis) {
                        Toast.makeText(context, R.string.select_seven_days_only, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        getData()
                        dismiss()
                    }
                }
            }

        }
        binding?.btnMonthly?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialog = MonthPickerBinding.inflate(layoutInflater)
                setContentView(dialog.root)
                show()
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.listView.setOnItemClickListener { parent, view, position, id ->
                    var selectedMonthPosition = position
                    dismiss()
                    startCalendar.set(
                        Calendar.getInstance().get(Calendar.YEAR),
                        position,
                        0,
                        0,
                        0,
                        0
                    )
                    endCalendar.set(
                        Calendar.getInstance().get(Calendar.YEAR),
                        position,
                        getLastDayOfMonth(Calendar.getInstance().get(Calendar.YEAR), position),
                        23,
                        59,
                        59
                    )
                    Log.e(
                        "TAG",
                        "Calendar date ${startCalendar.time} end Calendar ${endCalendar.time}"
                    )
                    getData()
                }
            }
        }
    }

    fun getLastDayOfMonth(year: Int, month: Int): Int {
        // Create a Calendar instance
        val calendar = Calendar.getInstance()

        // Set the year and month (months are 0-based, so subtract 1)
        calendar.set(year, month - 1, 1)

        // Get the maximum day of the month
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getData() {
        taskList.clear()
        taskList.addAll(
            tasksDatabase?.tasksDao()?.getTaskBetweenDates(
                startDate = startCalendar.timeInMillis,
                endDate = endCalendar.timeInMillis
            ) ?: arrayListOf()
        )
        showPieChart()
    }

    private fun showPieChart() {
        val pieEntries = ArrayList<PieEntry>()
        var completedTask = 0
        var incompleteTask = 0
        val colorSet = java.util.ArrayList<Int>()
        colorSet.add(Color.rgb(153,50,204))
        colorSet.add(Color.rgb(60,179,113))

        for (tasks in taskList) {
            if (tasks.isCompleted == true) {
                completedTask++
            } else {
                incompleteTask++
            }
        }

        var completePercent =
            calculateCompletePercent(totalTask = taskList.size, completed = completedTask)
        pieEntries.add(PieEntry(completePercent.toFloat(), "Completed Tasks"))
        pieEntries.add(PieEntry((100 - completePercent).toFloat(), "Incompleted Tasks"))

        val pieDataSet = PieDataSet(pieEntries, "\nComplete Incomplete Percent")
        pieDataSet.valueTextSize = 12f
        pieDataSet.setColors(colorSet)
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        binding?.pieChart?.setData(pieData)
        binding?.pieChart?.invalidate()
    }

    private fun calculateCompletePercent(totalTask: Int, completed: Int): Double {
        var percent = 0.0
        percent = (completed.toDouble() / totalTask.toDouble()) * 100
        return percent
    }


}
