package xyz.ridsoft.harumap

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

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

            db.execSQL(Routine.SQL_CREATE)
            db.execSQL(Task.SQL_CREATE)
        }
    }

    fun getRoutines(): ArrayList<Routine> {
        return getRoutines("SELECT * FROM Routine")
    }

    fun getRoutines(sql: String): ArrayList<Routine> {
        val cursor = db.rawQuery(sql, null)
        return toRoutines(cursor)
    }

    fun toRoutines(cursor: Cursor): ArrayList<Routine> {
        val routines: ArrayList<Routine> = arrayListOf()
        while (cursor.moveToNext()) {
            // Create routine object
            val routine = Routine(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("content"))
            )
            routine.enabled = cursor.getInt(cursor.getColumnIndex("enabled")) == 1
            routine.notification = cursor.getInt(cursor.getColumnIndex("notification")) == 1

            routines.add(routine)
        }

        return routines
    }

    fun getTasks(sql: String): ArrayList<Task> {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val week = cal.get(Calendar.WEEK_OF_YEAR)
        var cursor = db.rawQuery(sql, null)
        if (cursor.count == 0) {
            val json = "\"[0, 0, 0, 0, 0, 0, 0]\""
            db.execSQL("INSERT OR IGNORE INTO TASK (year, week, done) VALUES($year, $week, $json)")
            cursor = db.rawQuery(sql, null)
        }
        return toTasks(cursor)
    }

    fun getTask(year: Int, week: Int): Task {
        val sql =
            "SELECT * FROM Task WHERE year = $year AND week = $week;"

        val tasks = getTasks(sql)

        return tasks[0]
    }

    private fun toTasks(cursor: Cursor): ArrayList<Task> {
        val tasks: ArrayList<Task> = arrayListOf()
        while (cursor.moveToNext()) {
            tasks.add(
                Task(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getInt(cursor.getColumnIndex("year")),
                    cursor.getInt(cursor.getColumnIndex("week")),
                    Gson().fromJson(
                        cursor.getString(cursor.getColumnIndex("done")),
                        Array<Int>::class.java
                    )
                )
            )
        }
        cursor.close()

        return tasks
    }

    fun update(task: Task) {
        val sql =
            "INSERT OR REPLACE INTO Task (year, week, done, total) VALUES (" +
                    "${task.year}, " +
                    "${task.week}, " +
                    Gson().toJson(task.done) +
                    ") WHERE _id = ${task._id};"
        db.execSQL(sql)
    }

}