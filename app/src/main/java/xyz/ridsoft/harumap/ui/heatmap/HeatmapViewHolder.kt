package xyz.ridsoft.harumap.ui.heatmap

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.worker.DataManager
import xyz.ridsoft.harumap.R
import xyz.ridsoft.harumap.Task
import xyz.ridsoft.harumap.databinding.RowHeatmapBinding

class HeatmapViewHolder(private val binding: RowHeatmapBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_heatmap
    }

    fun bind(task: Task?, space: Boolean = false) {
        // Make empty space and return if it is space
        if (space) {
            binding.layoutRowHeatmap.visibility = View.INVISIBLE
            return
        }

        // Return if null data
        if (task == null) return

        var done = 0
        for (i in DataManager.routines.keys) {
            if (task.routines[i] == true) done++
        }

        val level: Float = if (done == 0 || DataManager.routines.isEmpty()) 0.0f
        else done.toFloat() / DataManager.routines.size.toFloat()

        when {
            level == 0f -> {
                binding.layoutRowHeatmap.setBackgroundResource(R.drawable.bg_level_0)
            }
            level <= 0.25 -> {
                binding.layoutRowHeatmap.setBackgroundResource(R.drawable.bg_level_1)
            }
            level <= 0.5 -> {
                binding.layoutRowHeatmap.setBackgroundResource(R.drawable.bg_level_2)
            }
            level <= 0.75 -> {
                binding.layoutRowHeatmap.setBackgroundResource(R.drawable.bg_level_3)
            }
            else -> {
                binding.layoutRowHeatmap.setBackgroundResource(R.drawable.bg_level_4)
            }
        }
    }
}