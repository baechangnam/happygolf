package com.artilearn.happygolfgo.ui.home.alram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.base.Identifiable
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.databinding.ItemHomeAlramBinding

typealias OnAlramItemClick = (Alram) -> Unit

class AlramAdapter(
    private val onItemClick: OnAlramItemClick? = null
) : BaseRecyclerViewAdapter<Alram>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Alram> {
        return AlramViewHolder(ItemHomeAlramBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )).also {
            if (onItemClick == null) {
                return@also
            } else {
                it.itemView.setOnClickListener { _ ->
                    val currentItem = currentList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                    onItemClick.invoke(currentItem)
                }
            }
        }
    }

    class AlramViewHolder(
        private val binding: ItemHomeAlramBinding
    ) : BaseViewHolder<Alram>(binding) {
        override fun bind(item: Alram) {
            super.bind(item)

            when (item.eventType) {
                "3", "4", "6", "7" -> binding.ivArrow.visibility = View.VISIBLE
                else -> binding.ivArrow.visibility = View.GONE
            }

            when (item.isRead) {
                false -> binding.root.setBackgroundResource(R.drawable.bg_alram_unread)
                true -> binding.root.setBackgroundResource(R.drawable.bg_alram_read)
            }
        }
    }
}