package com.creator.todolist.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.creator.todolist.AddNewTaskFragment
import com.creator.todolist.MainActivity
import com.creator.todolist.R
import com.creator.todolist.Utils.DatabaseHelper
import com.creator.todolist.model.Task

class ToDoAdapter(
    val activity: MainActivity,
    var tasks : MutableList<Task>,
    val db : DatabaseHelper
) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)

            .inflate(R.layout.task, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val task = tasks[position]
        holder.checkbox.text = task.text
        holder.checkbox.isChecked = toBoolean(task.status)
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                db.updateStatus(task.id, 1)
            else db.updateStatus(task.id, 0)
        }
    }
    fun getContext() : Context = activity
    fun deleteTask(position: Int) {
        val task = tasks.get(position)
        db.deleteTask(task.id)
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }
    fun toBoolean(n: Int) = n != 0

    override fun getItemCount(): Int {
        return tasks.size
    }
    fun editTask(position: Int) {
        val item = tasks.get(position)
        val bundle = Bundle()
        bundle.putInt("ID", item.id)
        bundle.putString("task", item.text)

        val task = AddNewTaskFragment()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)

    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)
    }
}