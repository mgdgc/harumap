package xyz.ridsoft.harumap.ui.heatmap

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.database.DBHelper
import xyz.ridsoft.harumap.worker.DataManager
import xyz.ridsoft.harumap.Task
import xyz.ridsoft.harumap.databinding.RowHeatmapBinding
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class HeatmapAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var spaces = 0
    private var data: ArrayList<Task?> = arrayListOf()

    init {
        initData()
        notifyItemRangeInserted(0, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HeatmapViewHolder(
            RowHeatmapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as HeatmapViewHolder

        if (spaces > position) {
            viewHolder.bind(null, true)
        } else {
            viewHolder.bind(this.data[position])
        }
    }

    private fun initData() {
        // Initialize DataManager
        DataManager(context)

        // Comparator for sorting (by _id)
        val comparator = Comparator { task1: Task, task2: Task ->
            task1._id.toInt() - task2._id.toInt()
        }
        // Get tasks from DataManager and sort
        val tasks = DataManager.tasks.values.sortedWith(comparator)

        var prevYear = tasks[0].year
        var prevDay = tasks[0].day

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, prevYear)
        cal.set(Calendar.DAY_OF_YEAR, prevDay)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        for (i in 1 until dayOfWeek) {
            spaces++
            data.add(null)
        }

        for (i in tasks.indices) {
            // When year changed
            if (tasks[i].year > prevYear) {
                cal.set(Calendar.YEAR, tasks[i].year - 1)
                // Fill null data between prevWeek ~ the maximum week the year can have
                for (j in prevDay until cal.getActualMaximum(Calendar.DAY_OF_YEAR)) {
                    data.add(null)
                }
                prevYear = tasks[i].year
                prevDay = 0
            }
            // Fill null data between prevWeek ~ this week
            for (j in prevDay + 1 until tasks[i].day) {
                data.add(null)
            }

            data.add(tasks[i])
            prevDay = tasks[i].day
        }
    }

    override fun getItemViewType(position: Int): Int {
        return HeatmapViewHolder.VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun routineUpdated() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val day = cal.get(Calendar.DAY_OF_YEAR)
        val task = DBHelper(context).getTask(year, day)
        this.data.removeLast()
        this.data.add(task)
        notifyItemChanged(this.data.lastIndex)
    }
}