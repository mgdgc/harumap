package xyz.ridsoft.harumap

import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.databinding.RowHeatmapBinding

class HeatmapViewHolder(private val binding: RowHeatmapBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_heatmap
    }

    fun bind(data: Task?) {
        if (data == null) return

        val heatmaps = arrayOf(
            binding.layoutSun,
            binding.layoutMon,
            binding.layoutTue,
            binding.layoutWed,
            binding.layoutThu,
            binding.layoutFri,
            binding.layoutSat
        )

        for (i in heatmaps.indices) {
            if (data.done[i] <= 1) {
                if (data.done[i] == 1) {
                    heatmaps[i].setBackgroundResource(R.drawable.bg_level_2)
                }
                continue
            }

            val level: Float = data.done[i].toFloat() / DataManager.routines.size
            when {
                level == 0f -> {
                    heatmaps[i].setBackgroundResource(R.drawable.bg_level_0)
                }
                level <= 0.25 -> {
                    heatmaps[i].setBackgroundResource(R.drawable.bg_level_1)
                }
                level <= 0.5 -> {
                    heatmaps[i].setBackgroundResource(R.drawable.bg_level_2)
                }
                level <= 0.75 -> {
                    heatmaps[i].setBackgroundResource(R.drawable.bg_level_3)
                }
                else -> {
                    heatmaps[i].setBackgroundResource(R.drawable.bg_level_4)
                }
            }
        }
    }
}