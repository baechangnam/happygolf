package com.artilearn.happygolfgo.ui.home.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ItemHomeReservationTicketBinding

typealias OnRecyclerViewItemClick = (Ticket) -> Unit

class ReservationAdapter(
    private val onItemClick: OnRecyclerViewItemClick? = null
) : ListAdapter<Ticket, ReservationAdapter.TicketViewHolder>(object : DiffUtil.ItemCallback<Ticket>() {
    override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
        return oldItem == newItem
    }
})  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        return TicketViewHolder.from(parent).also {
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

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class TicketViewHolder(private val binding: ItemHomeReservationTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ticket) {
            binding.item = item
            when (item.type) {
                1 -> binding.ivArrow.visibility = View.INVISIBLE
                else -> binding.ivArrow.visibility = View.VISIBLE
            }
            binding.executePendingBindings()
        }

        companion object Factory {
            fun from(parent: ViewGroup): TicketViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHomeReservationTicketBinding.inflate(layoutInflater, parent, false)

                return TicketViewHolder(binding)
            }
        }
    }
}