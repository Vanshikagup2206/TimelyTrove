package com.vanshika.tasktrack

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vanshika.tasktrack.databinding.FragmentTaskAdditionBinding
import com.vanshika.tasktrack.databinding.TaskAddedDialogBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskAdditionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskAdditionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentTaskAdditionBinding? = null
    var taskDataClass = TaskDataClass()
    var categoryList = arrayListOf<CategoryDataClass>()
    lateinit var arrayAdapter: ArrayAdapter<CategoryDataClass>
    lateinit var tasksDatabase: TasksDatabase
    var simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy")
    var timeFormat = SimpleDateFormat("hh:mm aa")
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    var taskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding = FragmentTaskAdditionBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())

        arguments?.let {
            taskId = it.getInt("taskId", 0)
            if (taskId > 0)
                getTaskInformation()
        }
        getCategoryList()

        binding?.etDate?.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { _, year, month, date ->
                    startCalendar.set(year, month, date)
                    endCalendar.set(year, month, date)
                    var formattedDate = simpleDateFormat.format(startCalendar.time)
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
        binding?.btnAdd?.setOnClickListener {
            if (categoryList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.enter_category),
                    Toast.LENGTH_LONG
                ).show()
            } else if (binding?.etEnterTaskTitle?.text.toString().isNullOrEmpty()) {
                binding?.etEnterTaskTitle?.error =
                    resources.getString(R.string.enter_task_title)
            } else if (binding?.etDate?.text.toString().isNullOrEmpty()) {
                binding?.etDate?.error =
                    resources.getString(R.string.enter_date)
            } else if (binding?.etStarts?.text.toString().isNullOrEmpty()) {
                binding?.etStarts?.error = resources.getString(R.string.enter_time)
            } else if (binding?.etEnds?.text.toString().isNullOrEmpty()) {
                binding?.etEnds?.error = resources.getString(R.string.enter_time)
            } else if (binding?.etDescription?.text.toString().isNullOrEmpty()) {
                binding?.etDescription?.error =
                    resources.getString(R.string.enter_description)
            } else {
                var selectedCategory =
                    binding?.spCategory?.selectedItem as CategoryDataClass
                if (taskId > 0) {
                } else {
                    var color = if (binding?.rbGreen?.isChecked == true) {
                        1
                    } else if (binding?.rbRed?.isChecked == true) {
                        2
                    } else if (binding?.rbOrange?.isChecked == true) {
                        3
                    } else {
                        0
                    }
                    tasksDatabase.tasksDao().insertTask(
                        TaskDataClass(
                            taskName = binding?.etEnterTaskTitle?.text.toString(),
                            taskDescription = binding?.etDescription?.text.toString(),
                            taskSubTask = binding?.etSubTasks?.text.toString(),
                            taskStartTime = startCalendar.time,
                            taskEndTime = endCalendar.time,
                            categoryId = selectedCategory.categoryId,
                            categoryName = selectedCategory.categoryName,
                            color = color
                        )
                    )
                    Dialog(requireContext()).apply {
                        var dialogBinding = TaskAddedDialogBinding.inflate(layoutInflater)
                        setContentView(dialogBinding.root)
                        window?.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        show()
                        dialogBinding.btnDone.setOnClickListener {
                            findNavController().popBackStack()
                            dismiss()
                        }
                    }
                }
            }
        }
        binding?.llAddCategory?.setOnClickListener {
            findNavController().navigate(R.id.categoryFragment)
        }
    }


    private fun getCategoryList() {
        categoryList.clear()
        categoryList.addAll(tasksDatabase.tasksDao().getCategory())
        arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryList)
        binding?.spCategory?.adapter = arrayAdapter
    }

    fun getTaskInformation() {
        taskDataClass = tasksDatabase.tasksDao().taskAccId(taskId) ?: TaskDataClass()
        binding?.etEnterTaskTitle?.setText(taskDataClass.taskName)
        binding?.etDescription?.setText(taskDataClass.taskDescription)
        binding?.etDate?.setText(simpleDateFormat.format(startCalendar.time))
        binding?.etStarts?.setText(timeFormat.format(startCalendar.time))
        binding?.etEnds?.setText(timeFormat.format(endCalendar.time))
        binding?.etSubTasks?.setText(taskDataClass.taskSubTask)
        binding?.btnAdd?.setText(resources.getString(R.string.update))
    }
}

