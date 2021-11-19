package xyz.ridsoft.harumap.worker

import android.content.Context
import xyz.ridsoft.harumap.Routine
import xyz.ridsoft.harumap.Task
import xyz.ridsoft.harumap.database.DBHelper

class DataManager(private val context: Context) {
    companion object {
        private var initialized = false
        var routines: MutableMap<Int, Routine> = mutableMapOf()
        var tasks: MutableMap<String, Task> = mutableMapOf()
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
        routines.clear()

        // Get data from db
        val routines = dbHelper.getRoutines()
        for (routine in routines) {
            Companion.routines[routine.id] = routine
        }
    }

    fun initTasks() {
        // Clear data
        tasks.clear()

        // Get data from db
        val tasks = dbHelper.getTasks("SELECT * FROM Task ORDER BY year ASC, day ASC")
        for (task in tasks) {
            Companion.tasks[task._id] = task
        }
    }
}