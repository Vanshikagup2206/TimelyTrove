package com.vanshika.tasktrack

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.vanshika.tasktrack.databinding.FragmentTaskUpdationBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskUpdationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskUpdationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentTaskUpdationBinding? = null
    var taskDataClass = TaskDataClass()
    var taskList = arrayListOf<TaskDataClass>()
    var categoryList = arrayListOf<CategoryDataClass>()
    lateinit var tasksDatabase: TasksDatabase
    lateinit var arrayAdapter: ArrayAdapter<CategoryDataClass>
    var simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy")
    var timeFormat = SimpleDateFormat("hh:mm aa")
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    var tasks = 0
    private val TAG = "TaskUpdationFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            tasks = it.getInt("tasks",0)
            Log.e(TAG, "onCreate: $tasks ", )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskUpdationBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        getCategoryList()

        if (tasks>0){
            getDataById(tasks)
            updateTask()
        }
        getTaskList()
        binding?.etDate?.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { _, year, month, date ->
                    startCalendar.set(year, month, date)
                    endCalendar.set(year, month, date)
                    val formattedDate = simpleDateFormat.format(startCalendar.time)
                    binding?.etDate?.setText(formattedDate)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }

        binding?.etStarts?.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                R.style.MyTimePickerStyle,
                { _, hour, minute ->
                    startCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    startCalendar.set(Calendar.MINUTE, minute)
                    binding?.etStarts?.setText(timeFormat.format(startCalendar.time))
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
            ).show()
        }

        binding?.etEnds?.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                R.style.MyTimePickerStyle,
                { _, hour, minute ->
                    endCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    endCalendar.set(Calendar.MINUTE, minute)
                    binding?.etEnds?.setText(timeFormat.format(endCalendar.time))
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    fun updateTask() {
        binding?.etEnterTaskTitle?.setText(taskDataClass.taskName)
        binding?.etDescription?.setText(taskDataClass.taskDescription)
        binding?.etDate?.setText(simpleDateFormat.format(taskDataClass.taskDate))
        binding?.etStarts?.setText(timeFormat.format(taskDataClass.taskStartTime))
        binding?.etEnds?.setText(timeFormat.format(taskDataClass.taskEndTime))
        binding?.etSubTasks?.setText(taskDataClass.taskSubTask)
        binding?.radioButton?.checkedRadioButtonId
        when(taskDataClass.color){
            1 -> binding?.rbGreen?.isChecked = true
            2 -> binding?.rbRed?.isChecked = true
            3 -> binding?.rbOrange?.isChecked = true

        }
        binding?.btnAdd?.setOnClickListener {
            if (categoryList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.enter_category),
                    Toast.LENGTH_LONG
                ).show()
            } else if (binding?.etEnterTaskTitle?.text.toString().trim().isEmpty()) {
                binding?.etEnterTaskTitle?.error =
                    resources.getString(R.string.enter_task_title)
            } else if (binding?.etDate?.text.toString().trim().isEmpty()) {
                binding?.etDate?.error =
                    resources.getString(R.string.enter_date)
            } else if (binding?.etStarts?.text.toString().trim().isEmpty()) {
                binding?.etStarts?.error = resources.getString(R.string.enter_time)
            } else if (binding?.etEnds?.text.toString().trim().isEmpty()) {
                binding?.etEnds?.error = resources.getString(R.string.enter_time)
            } else if (binding?.etDescription?.text.toString().trim().isEmpty()) {
                binding?.etDescription?.error = resources.getString(R.string.enter_description)
            } else {
                var color = if (binding?.rbGreen?.isChecked == true){
                    1
                }else if (binding?.rbRed?.isChecked == true){
                    2
                }else if (binding?.rbOrange?.isChecked == true) {
                    3
                }else{
                    0
                }
               val selectedItem=binding?.spCategory?.selectedItem as CategoryDataClass
                tasksDatabase.tasksDao().updateTask(
                    TaskDataClass(
                        taskId = tasks,
                        taskName = binding?.etEnterTaskTitle?.text.toString(),
                        taskDescription = binding?.etDescription?.text.toString(),
                        taskSubTask = binding?.etSubTasks?.text.toString(),
                        taskStartTime = startCalendar.time,
                        taskEndTime = endCalendar.time,
                        categoryId = selectedItem.categoryId,
                        categoryName = selectedItem.categoryName,
                        color = color
                    )
                )
                findNavController().popBackStack()
            }
        }

        getTaskList()
    }

    private fun getCategoryList() {
        categoryList.clear()
        categoryList.addAll(tasksDatabase.tasksDao().getCategory())
        arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryList)
        binding?.spCategory?.adapter = arrayAdapter
    }
    private fun getTaskList() {
        taskList.clear()
        taskList.addAll(tasksDatabase.tasksDao().getAllTask() ?: arrayListOf())
    }
    fun getDataById(position: Int){
        taskDataClass= tasksDatabase.tasksDao().getSingleTaskById(position)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskUpdationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskUpdationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}