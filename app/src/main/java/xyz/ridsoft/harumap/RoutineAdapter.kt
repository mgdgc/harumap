package xyz.ridsoft.harumap

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.databinding.RowFooterBinding
import xyz.ridsoft.harumap.databinding.RowRoutineBinding

class RoutineAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<Routine> = arrayListOf()
        set(value) {
            val prevSize = this.data.size
            this.data.clear()
            this.notifyItemRangeRemoved(0, prevSize)

            this.data.addAll(value)
            this.notifyItemRangeInserted(0, value.size)
        }

    fun add(routine: Routine) {
        this.data.add(routine)
        this.notifyItemInserted(this.data.lastIndex)
    }

    fun remove(position: Int) {
        this.data.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun removeLast() {
        this.data.removeAt(this.data.lastIndex)
    }

    var onStateChangedListener: ((id: Int, complete: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            RoutineViewHolder.VIEW_TYPE -> {
                RoutineViewHolder(
                    RowRoutineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> {
                FooterViewHolder(
                    RowFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoutineViewHolder) {
            holder.onStateChangedListener = { id, done ->
                onStateChangedListener?.let { it(id, done) }
            }
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return this.data.size + 1
    }
}