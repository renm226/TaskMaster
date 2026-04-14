package com.IT487.taskmaster

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val etTitle = findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = findViewById<TextInputEditText>(R.id.etDescription)
        val etDueDate = findViewById<TextInputEditText>(R.id.etDueDate)
        val spinnerPriority = findViewById<Spinner>(R.id.spinnerPriority)
        val btnSave = findViewById<Button>(R.id.btnSave)

        ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPriority.adapter = adapter
            }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val dueDate = etDueDate.text.toString().trim()
            val priority = spinnerPriority.selectedItem.toString()

            if (title.isEmpty()) {
                Toast.makeText(this, "Task title is required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = TaskDbHelper(this)
            dbHelper.insertTask(title, description, dueDate, priority)

            Toast.makeText(this, "Task added successfully!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}