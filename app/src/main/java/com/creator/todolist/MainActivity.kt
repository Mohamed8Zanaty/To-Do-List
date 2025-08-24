package com.creator.todolist

import android.content.ClipData
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creator.todolist.Utils.DatabaseHelper
import com.creator.todolist.adapter.ToDoAdapter
import com.creator.todolist.model.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() , OnDialogCloseListener{

    lateinit var tasksRecycle : RecyclerView
    lateinit var addTaskBtn : FloatingActionButton
    lateinit var db : DatabaseHelper
    lateinit var tasks : List<Task>
    lateinit var adapter: ToDoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tasksRecycle = findViewById(R.id.tasks)
        tasksRecycle.setHasFixedSize(true)
        tasksRecycle.layoutManager = LinearLayoutManager(this)
        addTaskBtn = findViewById(R.id.add_task_btn)
        db = DatabaseHelper(this)
        tasks = listOf()
        tasks = db.getAllTasks()
        tasks = tasks.reversed()
        adapter = ToDoAdapter(this, tasks.toMutableList(), db)
        tasksRecycle.adapter = adapter

        addTaskBtn.setOnClickListener {
            AddNewTaskFragment.newInstance().show(supportFragmentManager, AddNewTaskFragment.TAG)
        }
        val touchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        touchHelper.attachToRecyclerView(tasksRecycle)
    }

    override fun onDialogClose(dialog: DialogInterface) {
        tasks = db.getAllTasks()
        tasks = tasks.reversed()
        adapter.tasks = tasks.toMutableList()
        adapter.notifyDataSetChanged()
    }
}