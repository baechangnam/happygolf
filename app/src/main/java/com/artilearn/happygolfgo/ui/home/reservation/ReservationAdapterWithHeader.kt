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

typealias TicketItemClickWithHeaderView = (Ticket) -> Unit

class ReservationAdapterWithHeader(
    private val reservationViewModel :ReservationViewModel,
    private val onItemClick: TicketItemClickWithHeaderView? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<Ticket>()
    private var announcement  = Announcement()
    private var examination = Examination()

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
            HEADER_VIEW -> {
                ReservationHeaderViewHolder.create(parent).also {
//                    if (onItemClick == null) {
//                        return@also
//                    } else {
//                        it.itemView.setOnClickListener { _ ->
//                            val currentItem = items[it.adapterPosition]
//                            onItemClick.invoke(currentItem)
//                        }
//                    }
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

    class UserTicketHolder(private val binding: ItemReservationUseTicketBinding)
        : RecyclerView.ViewHolder(binding.root) {
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

    class ReservationHeaderViewHolder(private val binding: ItemReservationHeaderViewBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: Ticket) {
         fun bind(announcement: Announcement, examination: Examination, reservationViewModel: ReservationViewModel) {
            binding.announcement =  announcement
            binding.examination = examination
            binding.vm = reservationViewModel
            binding.executePendingBindings()
        }

        companion object HeaderViewFactory {
            fun create(parent: ViewGroup): ReservationHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemReservationHeaderViewBinding.inflate(layoutInflater, parent, false)
                return ReservationHeaderViewHolder(view)
            }
        }
    }

    fun addItems(items: List<Ticket>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    fun setAnnouncement(announcement: Announcement) {
        this.announcement = announcement
        this.notifyDataSetChanged()
    }

    fun setExamination(examination: Examination) {
        this.examination = examination
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
        const val HEADER_VIEW = 4
    }
}