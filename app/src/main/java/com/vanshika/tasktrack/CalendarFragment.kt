package com.vanshika.tasktrack

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.o7solutions.calendarviewlibrary.CalenderViewInterface
import com.vanshika.tasktrack.databinding.FragmentCalendarBinding
import java.util.Calendar
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentCalendarBinding? = null
    lateinit var database: TasksDatabase
    lateinit var mainActivity: MainActivity
    var taskDataclass = arrayListOf<TaskDataClass>()
    var events = arrayListOf<Calendar>()
    private val TAG = "CalendarFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        database = TasksDatabase.getInstance(requireContext())
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
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.calendar?.setEventHandler(object : CalenderViewInterface.EventHandler {

            override fun onDayClick(view: View?, date: Date, position: Int) {

            }

            override fun onDayLongClick(view: View?, date: Date, position: Int) {

            }

            override fun onMonthClick(view: View?, month: String, position: Int) {
                getMonthData(position)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getMonthData(Calendar.getInstance().get(Calendar.MONTH))
    }

    fun getMonthData(position: Int){
        taskDataclass.clear()
        events.clear()
        Log.e(TAG, " position of the month $position")
        var startcalendar = Calendar.getInstance()
        startcalendar.set(Calendar.DATE, 1)
        startcalendar.set(Calendar.MONTH, position)
        startcalendar.set(Calendar.HOUR_OF_DAY, 0)
        startcalendar.set(Calendar.MINUTE, 0)
        startcalendar.set(Calendar.SECOND, 0)

        var endCalendar = Calendar.getInstance()
        endCalendar.set(Calendar.DATE, getLastDayOfMonth(Calendar.getInstance().get(Calendar.YEAR), (Calendar.getInstance().get(Calendar.MONTH)+1)))
        endCalendar.set(Calendar.MONTH, position)
        endCalendar.set(Calendar.HOUR_OF_DAY, 23)
        endCalendar.set(Calendar.MINUTE, 59)
        endCalendar.set(Calendar.SECOND, 59)

        taskDataclass.addAll(database.tasksDao().getTaskBetweenDates(startDate = startcalendar.timeInMillis, endDate = endCalendar.timeInMillis))
        for (tasks in taskDataclass){
            val calendar = Calendar.getInstance()
            calendar.time = tasks.taskStartTime!!
            events.add(calendar)
        }
        Log.e(TAG, "events ${events.size}")
        binding?.calendar?.builder()
            ?.withEvents(
                events = events,
                eventDotColor = R.color.blue
            )
            ?.buildCalendar()

    }

    fun getLastDayOfMonth(year: Int, month: Int): Int {
        // Create a Calendar instance
        val calendar = Calendar.getInstance()

        // Set the year and month (months are 0-based, so subtract 1)
        calendar.set(year, month - 1, 1)

        // Get the maximum day of the month
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}