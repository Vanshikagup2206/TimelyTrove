package com.vanshika.tasktrack

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class TaskRecyclerAdapter(
    var context: Context,
    var taskList: ArrayList<TaskDataClass>,
    var taskClickInterface: TaskClickInterface
) : RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvtasks: TextView = view.findViewById(R.id.tvTasks)
        var tvTaskDescription: TextView = view.findViewById(R.id.tvTaskDescription)
        var tvCategory: TextView = view.findViewById(R.id.tvCategory)
        var tvSubTasks: TextView = view.findViewById(R.id.tvSubTasks)
        var tvDate: TextView = view.findViewById(R.id.tvDate)
        var tvStart: TextView = view.findViewById(R.id.tvStart)
        var tvEnd: TextView = view.findViewById(R.id.tvEnds)
        var ibUpdate: ImageView = view.findViewById(R.id.ibUpdate)
        var ibDelete: ImageView = view.findViewById(R.id.ibDelete)
        var cvTasks: CardView = view.findViewById(R.id.cvTask)
        var cbTask: CheckBox = view.findViewById(R.id.cbTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.tasks_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvtasks.setText(taskList[position].taskName)
        holder.tvTaskDescription.setText(taskList[position].taskDescription)
        holder.tvCategory.setText(taskList[position].categoryName)
        holder.tvSubTasks.setText(taskList[position].taskSubTask)
        //   holder.tvDate.setText(SimpleDateFormat("dd/MMM/YYYY").format(taskList[position].taskStartTime))
//        holder.tvStart.setText(SimpleDateFormat("hh:mm aa").format(taskList[position].taskStartTime))
//        holder.tvEnd.setText(SimpleDateFormat("hh:mm aa").format(taskList[position].taskEndTime))
        when (taskList[position].color) {
            1 ->
                holder.cvTasks.setCardBackgroundColor(ContextCompat.getColor(context,R.color.lightGreen))
            2 ->
                holder.cvTasks.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple))
            3 ->
                holder.cvTasks.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pink))
        }
        holder.cbTask.setOnClickListener {
            taskClickInterface.
            isCompleted(position = position, holder.cbTask.isChecked ?: false)
        }

        holder.cbTask.isChecked = taskList[position].isCompleted ?: false
        if (taskList[position].isCompleted == true) {
            if (holder.cbTask.isChecked) {
                holder.tvtasks.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvCategory.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvTaskDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvStart.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvEnd.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvSubTasks.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                holder.tvtasks.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.tvCategory.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.tvTaskDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.tvDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.tvStart.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.tvEnd.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.tvSubTasks.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        holder.ibUpdate.setOnClickListener {
            taskClickInterface.updateTask(position)
        }
        holder.ibDelete.setOnClickListener {
            taskClickInterface.deleteTask(position)
        }
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            context.resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        var dateFormat = sharedPreferences.getString("dateTimeFormat", "dd/MMM/yyyy").toString()
        holder.tvDate.setText(SimpleDateFormat(dateFormat).format(taskList[position].taskStartTime))

        var timeFormat = sharedPreferences.getString("timeFormat", "hh:mm aa")
        holder.tvStart.setText(SimpleDateFormat(timeFormat).format(taskList[position].taskStartTime))
        holder.tvEnd.setText(SimpleDateFormat(timeFormat).format(taskList[position].taskEndTime))
    }
}