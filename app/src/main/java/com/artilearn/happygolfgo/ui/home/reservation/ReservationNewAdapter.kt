package com.artilearn.happygolfgo.ui.home.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ItemReservationExpiredTicketBinding
import com.artilearn.happygolfgo.databinding.ItemReservationPauseTicketBinding
import com.artilearn.happygolfgo.databinding.ItemReservationUseTicketBinding

typealias TicketItemClick = (Ticket) -> Unit

class ReservationNewAdapter(
    private val onItemClick: TicketItemClick? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USE_TICKET -> {
                UserTicketHolder.create(parent).also {
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
            PAUSE_TICKET -> {
                PauseTicketHolder.create(parent).also {
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
            EXPIRED_TICKET -> {
                ExpiredTicketHolder.create(parent).also {
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
            else -> throw RuntimeException("Not Found Reservation ViewHolder ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = items[position]

        when (items[position].viewType) {
            USE_TICKET -> {
                (holder as UserTicketHolder).bind(type)
            }
            PAUSE_TICKET -> {
                (holder as PauseTicketHolder).bind(type)
            }
            EXPIRED_TICKET -> {
                (holder as ExpiredTicketHolder).bind(type)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    class UserTicketHolder(private val binding: ItemReservationUseTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ticket) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object UseFactory {
            fun create(parent: ViewGroup): UserTicketHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemReservationUseTicketBinding.inflate(layoutInflater, parent, false)

                return UserTicketHolder(view)
            }
        }
    }

    class PauseTicketHolder(private val binding: ItemReservationPauseTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ticket) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object PauseFactory {
            fun create(parent: ViewGroup): PauseTicketHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemReservationPauseTicketBinding.inflate(layoutInflater, parent, false)

                return PauseTicketHolder(view)
            }
        }
    }

    class ExpiredTicketHolder(private val binding: ItemReservationExpiredTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ticket) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object ExpiredFactory {
            fun create(parent: ViewGroup): ExpiredTicketHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemReservationExpiredTicketBinding.inflate(layoutInflater, parent, false)

                return ExpiredTicketHolder(view)
            }
        }
    }

    fun addItems(items: List<Ticket>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    fun addItem(item: Ticket) {
        this.items.add(item)
        this.notifyDataSetChanged()
    }

    companion object {
        const val USE_TICKET = 1
        const val PAUSE_TICKET = 2
        const val EXPIRED_TICKET = 3
    }
}