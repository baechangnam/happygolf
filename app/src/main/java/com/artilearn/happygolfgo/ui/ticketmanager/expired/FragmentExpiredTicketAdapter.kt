package com.artilearn.happygolfgo.ui.ticketmanager.expired

import android.view.LayoutInflater
import android.view.ViewGroup
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.databinding.ItemExpiredTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.model.ExpiredModel

typealias OnRecyclerViewItemClcik = (ExpiredModel) -> Unit

class FragmentExpiredTicketAdapter(
    private val onItemClick: OnRecyclerViewItemClcik? = null
) : BaseRecyclerViewAdapter<ExpiredModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ExpiredModel> {
        return ItemViewHolder(
            ItemExpiredTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also {
            it.itemView.setOnClickListener { _ ->
                if (onItemClick == null) {
                    return@setOnClickListener
                } else {
                    val currentItem = currentList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                    onItemClick.invoke(currentItem)
                }
            }
        }
    }

    class ItemViewHolder(
        private val binding: ItemExpiredTicketBinding
    ) : BaseViewHolder<ExpiredModel>(binding) {
        override fun bind(item: ExpiredModel) {
            super.bind(item)

        }
    }
}