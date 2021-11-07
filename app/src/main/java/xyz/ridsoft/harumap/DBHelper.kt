package xyz.ridsoft.harumap

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity

class DBHelper(private val context: Context) {

    companion object {
        var initialized = false
        lateinit var db: SQLiteDatabase
    }

    init {
        if (!initialized) {
            db = context.openOrCreateDatabase(
                DB.DB, AppCompatActivity.MODE_PRIVATE, null
            )
        }
    }

    fun getTasks(sql: String): ArrayList<Task> {
        val cursor = db.rawQuery(sql, null)
        return toTasks(cursor)
    }

    fun getTasks(week: Int): ArrayList<Task> {
        val sql = "SELECT * FROM Task WHERE week = $week;"
        return getTasks(sql)
    }

    private fun toTasks(cursor: Cursor): ArrayList<Task> {
        val tasks: ArrayList<Task> = arrayListOf()
        while (cursor.moveToNext()) {
            tasks.add(
                Task(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("week")),
                    cursor.getInt(cursor.getColumnIndex("dayOfWeek")),
                    cursor.getInt(cursor.getColumnIndex("complete")) == 1
                )
            )
        }
        cursor.close()

        return tasks
    }

    fun update(task: Task) {
        val sql = "UPDATE Task SET complete = ${task.complete} WHERE id = ${task._id};"
        db.execSQL(sql)
    }

}