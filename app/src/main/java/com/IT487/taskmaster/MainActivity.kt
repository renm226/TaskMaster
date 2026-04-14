package com.IT487.taskmaster

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDbHelper
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDbHelper(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val fab: FloatingActionButton = findViewById(R.id.fabAdd)
        val etSearch: TextInputEditText = findViewById(R.id.etSearch)

        adapter = TaskAdapter(taskList,
            onItemClick = { task ->
                val intent = Intent(this, TaskDetailActivity::class.java)
                intent.putExtra("TASK_ID", task.id)
                startActivity(intent)
            },
            onStatusChange = { task, completed ->
                dbHelper.updateCompleted(task.id, completed)
                loadTasks()
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
            })

        recyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchTasks(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loadTasks()
    }

    private fun loadTasks() {
        val cursor = dbHelper.getAllTasks()
        taskList.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_TITLE))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DESCRIPTION)) ?: ""
            val due = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DUE_DATE)) ?: ""
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_PRIORITY)) ?: "Medium"
            val completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_IS_COMPLETED)) == 1

            taskList.add(Task(id, title, desc, due, priority, completed))
        }
        cursor.close()
        adapter.updateTasks(taskList)
    }

    private fun searchTasks(query: String) {
        val cursor = if (query.isEmpty()) dbHelper.getAllTasks() else dbHelper.searchTasks(query)
        taskList.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_TITLE))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DESCRIPTION)) ?: ""
            val due = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DUE_DATE)) ?: ""
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_PRIORITY)) ?: "Medium"
            val completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_IS_COMPLETED)) == 1
            taskList.add(Task(id, title, desc, due, priority, completed))
        }
        cursor.close()
        adapter.updateTasks(taskList)
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }
}
