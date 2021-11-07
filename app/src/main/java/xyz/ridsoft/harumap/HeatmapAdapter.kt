package xyz.ridsoft.harumap

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.databinding.RowHeatmapBinding

class HeatmapAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<Week> = arrayListOf()
        set(value) {
            val prevSize = this.data.size
            this.data.clear()
            this.notifyItemRangeRemoved(0, prevSize)

            this.data.addAll(value)
            this.notifyItemRangeInserted(0, value.size)
        }

    fun add(week: Week) {
        this.data.add(week)
        this.notifyItemInserted(this.data.size - 1)
    }

    fun removeLast() {
        this.data.removeLast()
        this.notifyItemRemoved(this.data.size - 1)
    }

    fun set(week: Week) {
        this.data[week.week] = week
        this.notifyItemChanged(week.week)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HeatmapViewHolder(
            RowHeatmapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as HeatmapViewHolder
        viewHolder.bind(this.data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return HeatmapViewHolder.VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return this.data.size
    }
}