package com.artilearn.happygolfgo.ui.ticketmanager.pause

import android.view.LayoutInflater
import android.view.ViewGroup
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.databinding.ItemPauseTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.model.PauseModel

typealias OnRecyclerViewItemClick = (PauseModel) -> Unit

class FragmentPauseTicketAdapter(
    private val onItemClick: OnRecyclerViewItemClick? = null
) : BaseRecyclerViewAdapter<PauseModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PauseModel> {
        return ItemViewHolder(
            ItemPauseTicketBinding.inflate(
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
        private val binding: ItemPauseTicketBinding
    ) : BaseViewHolder<PauseModel>(binding) {
        override fun bind(item: PauseModel) {
            super.bind(item)
        }
    }

}