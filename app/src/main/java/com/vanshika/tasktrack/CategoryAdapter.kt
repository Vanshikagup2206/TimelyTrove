package com.vanshika.tasktrack

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    var context: Context,
    var categoryList: ArrayList<CategoryDataClass>,
    var categoryClickInterface: CategoryClickInterface
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var selectedPosition = 0

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var btnCategory: Button = view.findViewById(R.id.btnCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_addition_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnCategory.setText(categoryList[position].categoryName)
        holder.btnCategory.setOnClickListener {
            categoryClickInterface.onItemClick(position)
        }
        holder.itemView.setOnClickListener {}

        if (position == selectedPosition) {
            holder.btnCategory.setTextColor(context.getColor(R.color.white))
        } else {
            holder.btnCategory.setTextColor(context.getColor(R.color.black))
        }
    }

    fun updatePosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()

    }
}