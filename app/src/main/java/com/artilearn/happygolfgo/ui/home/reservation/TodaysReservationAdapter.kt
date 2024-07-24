package com.artilearn.happygolfgo.ui.home.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.announcement.Announcement
import com.artilearn.happygolfgo.data.examination.Examination
import com.artilearn.happygolfgo.databinding.ItemReservationExpiredTicketBinding
import com.artilearn.happygolfgo.databinding.ItemReservationHeaderViewBinding
import com.artilearn.happygolfgo.databinding.ItemReservationPauseTicketBinding
import com.artilearn.happygolfgo.databinding.ItemReservationUseTicketBinding
import com.artilearn.happygolfgo.databinding.ItemTodaysReservationBinding

typealias TodaysReservationItemClick = (String) -> Unit

class TodaysReservationAdapter(
    private val onItemClick: TodaysReservationItemClick? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodaysReservationViewHolder.create(parent).also {
            if (onItemClick == null) {
                return@also
            } else {
                it.itemView.setOnClickListener { _ ->
                    val currentItem = items[it.adapterPosition]
                    onItemClick.invoke(currentItem)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = items[position]
        (holder as TodaysReservationViewHolder).bind(type)
    }

    override fun getItemCount() = items.size

    class TodaysReservationViewHolder(private val binding: ItemTodaysReservationBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvReservation.text = item
        }

        companion object UseFactory {
            fun create(parent: ViewGroup): TodaysReservationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemTodaysReservationBinding.inflate(layoutInflater, parent, false)

                return TodaysReservationViewHolder(view)
            }
        }
    }

    fun addItems(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    fun addItem(item: String) {
        this.items.add(item)
        this.notifyDataSetChanged()
    }

}