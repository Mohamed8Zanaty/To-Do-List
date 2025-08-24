package com.creator.todolist

import android.content.ClipData
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
    lateinit var emptylist : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        setupRecyclerView()
        setupClickListeners()
        loadTasks()
    }

    override fun onDialogClose(dialog: DialogInterface) {
        loadTasks()

    }
    private fun initViews() {
        emptylist = findViewById(R.id.empty_list)
        tasksRecycle = findViewById(R.id.tasks)
        addTaskBtn = findViewById(R.id.add_task_btn)
    }
    private fun setupRecyclerView() {
        tasksRecycle.setHasFixedSize(true)
        tasksRecycle.layoutManager = LinearLayoutManager(this)

        db = DatabaseHelper(this)
        adapter = ToDoAdapter(this, mutableListOf(), db)
        tasksRecycle.adapter = adapter

        // Set up empty state callback
        adapter.onEmptyState = { isEmpty ->
            updateEmptyStateVisibility(isEmpty)
        }

        val touchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        touchHelper.attachToRecyclerView(tasksRecycle)
    }

    private fun setupClickListeners() {
        addTaskBtn.setOnClickListener {
            AddNewTaskFragment.newInstance().show(supportFragmentManager, AddNewTaskFragment.TAG)
        }
    }

    private fun loadTasks() {
        tasks = db.getAllTasks().reversed()
        adapter.updateTasks(tasks.toMutableList())
        updateEmptyStateVisibility(tasks.isEmpty())
    }

    private fun updateEmptyStateVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            emptylist.visibility = View.VISIBLE
            tasksRecycle.visibility = View.GONE
        } else {
            emptylist.visibility = View.GONE
            tasksRecycle.visibility = View.VISIBLE
        }
    }

}