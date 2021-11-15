package xyz.ridsoft.harumap

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.databinding.RowHeatmapBinding
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class HeatmapAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val cal = Calendar.getInstance()
        if (data[position]?.year == cal.get(Calendar.YEAR) &&
            data[position]?.day == cal.get(Calendar.DAY_OF_YEAR)
        ) {
            viewHolder.bind(this.data[position])
        } else {
            viewHolder.bind(this.data[position])
        }
    }

    private fun initData() {
        // Initialize DataManager
        DataManager(context)

        // Comparator for sorting (by _id)
        val comparator = Comparator { task1: Task, task2: Task -> task1._id - task2._id }
        // Get tasks from DataManager and sort
        val tasks = DataManager.tasks.values.sortedWith(comparator)

        var prevYear = tasks[0].year
        var prevWeek = tasks[0].day
        for (i in tasks.indices) {
            // When year changed
            if (tasks[i].year > prevYear) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, tasks[i].year)
                // Fill null data between prevWeek ~ the maximum week the year can have
                for (j in prevWeek until cal.getActualMaximum(Calendar.DAY_OF_YEAR)) {
                    data.add(null)
                }
                prevYear = tasks[i].year
                prevWeek = 0
            }
            // Fill null data between prevWeek ~ this week
            for (j in prevWeek until tasks[i].day) {
                data.add(null)
            }

            data.add(tasks[i])
            prevWeek = tasks[i].day
        }
    }

    override fun getItemViewType(position: Int): Int {
        return HeatmapViewHolder.VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return this.data.size
    }
}