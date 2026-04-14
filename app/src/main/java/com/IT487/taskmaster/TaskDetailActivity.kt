package com.IT487.taskmaster

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {

    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        taskId = intent.getIntExtra("TASK_ID", -1)

        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDescription)
        val tvDue = findViewById<TextView>(R.id.tvDetailDueDate)
        val tvPriority = findViewById<TextView>(R.id.tvDetailPriority)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val dbHelper = TaskDbHelper(this)
        val cursor = dbHelper.getTaskById(taskId)

        if (cursor.moveToFirst()) {
            tvTitle.text = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_TITLE))
            tvDesc.text =
                cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DESCRIPTION))
                    ?: "No description"
            tvDue.text =
                "Due: " + (cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DUE_DATE))
                    ?: "No date")
            tvPriority.text =
                "Priority: " + (cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_PRIORITY))
                    ?: "Medium")
        } else {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show()
            finish()
        }
        cursor.close()

        btnDelete.setOnClickListener {
            val dialog = DeleteConfirmDialogFragment { confirmed ->
                if (confirmed) {
                    val rows = dbHelper.deleteTask(taskId)
                    if (rows > 0) {
                        Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            dialog.show(supportFragmentManager, "delete_dialog")
        }
    }
}