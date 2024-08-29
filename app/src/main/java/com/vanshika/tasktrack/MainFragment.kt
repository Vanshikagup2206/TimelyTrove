package com.vanshika.tasktrack

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanshika.tasktrack.databinding.FragmentMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), TaskClickInterface, CategoryClickInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentMainBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var linearLayoutManagerCategory: LinearLayoutManager
    var taskList = arrayListOf<TaskDataClass>()
    var categoryList = arrayListOf<CategoryDataClass>()
    lateinit var adapter: TaskRecyclerAdapter
    lateinit var tasksDatabase: TasksDatabase
    lateinit var categoryAdapter: CategoryAdapter
    private val TAG = "MainFragment"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        adapter = TaskRecyclerAdapter(requireContext(), taskList, this)
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.rvTasks?.layoutManager = linearLayoutManager
        binding?.rvTasks?.adapter = adapter
        getTaskList()

        linearLayoutManagerCategory =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.rvCategory?.layoutManager = linearLayoutManagerCategory
        categoryAdapter = CategoryAdapter(requireContext(), categoryList, this)
        binding?.rvCategory?.adapter = categoryAdapter
        getCategoryList()

        binding?.fab?.setOnClickListener {
            findNavController().navigate(R.id.taskAdditionFragment)
        }
        sharedPreferences = requireContext().getSharedPreferences(resources.getString(R.string.app_name),MODE_PRIVATE)

    }

    private fun getTaskList() {
        taskList.clear()
        taskList.addAll(tasksDatabase.tasksDao().getAllTask() ?: arrayListOf())
        adapter.notifyDataSetChanged()
        if (taskList.size == 0) {
            binding?.vsEmptyView?.visibility = View.VISIBLE
        } else {
            binding?.vsEmptyView?.visibility = View.GONE
        }
    }

    private fun getCategoryList() {
        categoryList.clear()
        categoryList.add(CategoryDataClass(-1, "All"))
        categoryList.addAll(tasksDatabase.tasksDao().getCategory())
        categoryAdapter.notifyDataSetChanged()
    }

    override fun updateTask(position: Int) {
        findNavController().navigate(
            R.id.taskUpdationFragment,
            bundleOf("tasks" to taskList[position].taskId)
        )
    }

    override fun deleteTask(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.are_you_sure_you_want_to_delete_this))
        alertDialog.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
            tasksDatabase.tasksDao().deleteTask(taskList[position])
            getTaskList()
        }
        alertDialog.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
        }
        alertDialog.show()
    }

    override fun isCompleted(position: Int, isChecked: Boolean) {
        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(R.string.have_you_completed_your_task)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->
            taskList[position].isCompleted = true
            tasksDatabase.tasksDao().updateTask(taskList[position])
            adapter.notifyItemChanged(position)
            findNavController().navigate(R.id.rewardAddedFragment)
            addStars()
        }
        alertDialog.setNegativeButton(R.string.no) { _, _ ->
            taskList[position].isCompleted = false
            tasksDatabase.tasksDao().updateTask(taskList[position])
            adapter.notifyItemChanged(position)
            substars()
        }
        alertDialog.show()
    }

    override fun onItemClick(position: Int) {
        categoryAdapter.updatePosition(position)
        taskList.clear()
        if (categoryList[position].categoryId == -1) {
            getTaskList()
        } else {
            Log.e(TAG, "categoryList[position].categoryId ${categoryList[position].categoryId}")
            taskList.addAll(
                tasksDatabase.tasksDao().getTaskAccCat(categoryList[position].categoryId)
            )
            adapter.notifyDataSetChanged()
        }
    }
    private fun addStars() {
        val currentStars = sharedPreferences.getInt("stars", 0)
        val newStarCount = currentStars + 10
        sharedPreferences.edit().putInt("stars", newStarCount).apply()
    }
    private fun substars() {
        val currentStars = sharedPreferences.getInt("stars", 0)
        val newStarCount = currentStars - 10
        sharedPreferences.edit().putInt("stars", newStarCount).apply()
    }
}