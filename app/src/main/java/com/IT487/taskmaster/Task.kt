package com.IT487.taskmaster

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: String,
    val isCompleted: Boolean
)