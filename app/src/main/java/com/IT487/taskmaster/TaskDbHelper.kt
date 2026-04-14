package com.IT487.taskmaster

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "taskmaster.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_TASKS = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DUE_DATE = "due_date"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_IS_COMPLETED = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DUE_DATE TEXT,
                $COLUMN_PRIORITY TEXT DEFAULT 'Medium',
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun insertTask(title: String, description: String, dueDate: String, priority: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_DUE_DATE, dueDate)
            put(COLUMN_PRIORITY, priority)
        }
        return db.insert(TABLE_TASKS, null, values)
    }

    fun getAllTasks(): Cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_TASKS ORDER BY $COLUMN_ID DESC", null)

    fun searchTasks(query: String): Cursor = readableDatabase.rawQuery(
        "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_TITLE LIKE ? ORDER BY $COLUMN_ID DESC",
        arrayOf("%$query%")
    )

    fun deleteTask(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_TASKS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getTaskById(id: Int): Cursor = readableDatabase.rawQuery(
        "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_ID = ?",
        arrayOf(id.toString())
    )

    fun updateCompleted(id: Int, completed: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_COMPLETED, if (completed) 1 else 0)
        }
        return db.update(TABLE_TASKS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
