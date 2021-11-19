package xyz.ridsoft.harumap.ui.routine

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.database.DBHelper
import xyz.ridsoft.harumap.ui.FooterViewHolder
import xyz.ridsoft.harumap.Routine
import xyz.ridsoft.harumap.databinding.RowFooterBinding
import xyz.ridsoft.harumap.databinding.RowRoutineBinding
import java.util.*
import kotlin.collections.ArrayList

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

    fun add(routines: Array<Routine>) {
        val prevSize = this.data.size
        this.data.addAll(routines)
        this.notifyItemRangeInserted(prevSize, this.data.lastIndex)
    }

    fun add(routine: Routine) {
        this.data.add(routine)
        this.notifyItemInserted(this.data.lastIndex)
    }

    fun remove(position: Int) {
        this.data.removeAt(position)
        this.notifyItemRemoved(position)
    }

    var onLongClickListener: ((routine: Routine, position: Int) -> Unit)? = null
    var onStateChangedListener: ((id: Int, complete: Boolean) -> Unit)? = null

    private val calendar = Calendar.getInstance()

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

            holder.onStateChangedListener = onStateChangedListener
            holder.onLongClickListener = { r ->
                onLongClickListener?.let { it(r, position) }
            }

            val task = DBHelper(context).getTask(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.DAY_OF_YEAR)
            )
            holder.done = task.routines[data[position].id] == true

            holder.bind(data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < this.data.size) RoutineViewHolder.VIEW_TYPE
        else FooterViewHolder.VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return this.data.size + 1
    }
}