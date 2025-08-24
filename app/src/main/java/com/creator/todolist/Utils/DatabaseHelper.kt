package com.creator.todolist.Utils

import android.R
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.creator.todolist.model.Task

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,1) {
    companion object {
        private val DATABASE_NAME = "TODO"
        private val TABLE_NAME = "TODO"
        private val COL_1 = "ID"
        private val COL_2 = "TASK"
        private val COL_3 = "STATUS"


    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db);

    }
    fun addTask(task: Task) {
        val db  = writableDatabase
        val content = ContentValues()
        content.put(COL_2, task.text)
        content.put(COL_3, 0)
        db.insert(TABLE_NAME, null, content)
    }
    fun updateTask(id: Int, task : String) {
        val db  = writableDatabase
        val content = ContentValues()
        content.put(COL_2, task)
        db.update(TABLE_NAME, content, "ID=?", Array(1){"$id"})
    }
    fun updateStatus(id: Int, status : Int) {
        val db  = writableDatabase
        val content = ContentValues()
        content.put(COL_3, status)
        db.update(TABLE_NAME, content, "ID=?", Array(1){"$id"})
    }
    fun deleteTask(id: Int) {
        val db  = writableDatabase

        db.delete(TABLE_NAME,"ID=?", Array(1){"$id"})
    }

    fun getAllTasks() : List<Task> {
        val db  = readableDatabase
        var cursor : Cursor? = null
        val tasks = mutableListOf<Task>()
        db.beginTransaction()
        try {
            cursor = db.query(TABLE_NAME, null, null,null,null,null,null)
            if(cursor.moveToFirst()) {
                do {
                    val task = Task(
                        cursor.getInt(cursor.getColumnIndex(COL_1)),
                        cursor.getString(cursor.getColumnIndex(COL_2)),
                        cursor.getInt(cursor.getColumnIndex(COL_3)),

                    )
                    tasks.add(task)
                }while (cursor.moveToNext())
            }

        }finally {
            db.endTransaction()
            cursor?.close()
        }
            return tasks;
    }
}