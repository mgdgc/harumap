package xyz.ridsoft.harumap.ui

import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.R
import xyz.ridsoft.harumap.databinding.RowFooterBinding

class FooterViewHolder(binding: RowFooterBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_footer
    }
}