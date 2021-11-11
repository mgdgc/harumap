package xyz.ridsoft.harumap

import android.content.Context
import java.util.*

class RoutineManager(private val context: Context) {

    private val routinePref = context.getSharedPreferences(SharedPreferenceKeys.KEY_ROUTINES, 0)

    fun initRoutineCounts() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        // Check routine count has updated today
        if (routinePref.getInt(SharedPreferenceKeys.INT_YEAR, cal.get(Calendar.YEAR)) == year
            && routinePref.getInt(SharedPreferenceKeys.INT_DAY_OF_YEAR, 0) == dayOfYear) {
            return
        }

        DataManager(context)
        val edit = routinePref.edit()

        // Update date
        edit.putInt(SharedPreferenceKeys.INT_YEAR, year)
        edit.putInt(SharedPreferenceKeys.INT_DAY_OF_YEAR, dayOfYear)

        // Clear count
        for (r in DataManager.routines.keys) {
            edit.putBoolean(r.toString(), false)
        }

        // Commit and apply changes
        edit.apply()

    }

}