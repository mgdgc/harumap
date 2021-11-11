package xyz.ridsoft.harumap

import android.content.Context
import java.util.*

class DataManager(private val context: Context) {
    companion object {
        private var initialized = false
        var routines: MutableMap<Int, Routine> = mutableMapOf()
        var tasks: MutableMap<Int, Task> = mutableMapOf()
    }

    private val dbHelper = DBHelper(context)

    init {
        if (!initialized) {
            // Initialize routines
            initRoutines()

            // Initialize tasks
            initTasks()
        }
    }

    fun initRoutines() {
        // Clear data
        DataManager.routines.clear()

        // Get data from db
        val routines = dbHelper.getRoutines()
        for (routine in routines) {
            DataManager.routines[routine.id] = routine
        }
    }

    fun initTasks() {
        // Clear data
        DataManager.tasks.clear()

        // Get data from db
        val tasks = dbHelper.getTasks("SELECT * FROM Task ORDER BY ASC")
        for (task in tasks) {
            DataManager.tasks[task._id] = task
        }
    }
}