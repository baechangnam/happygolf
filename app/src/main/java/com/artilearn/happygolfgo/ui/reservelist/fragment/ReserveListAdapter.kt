package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.ItemReservationNewListBinding

class ReserveListAdapter(
    private val itemClick: (ReserveList) -> Unit
) : RecyclerView.Adapter<ReserveListAdapter.ListViewHolder>() {

    lateinit var binding: ItemReservationNewListBinding
    private val items: ArrayList<ReserveList> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            //doneTODO: 내 타석 예약 리스트
            R.layout.item_reservation_new_list,
            parent,
            false
        )

        return ListViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount() = items.size

    class ListViewHolder(
        private val binding: ItemReservationNewListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReserveList) {
            binding.item = item
            binding.executePendingBindings()
        }

    }

    fun setItems(lesson: List<ReserveList>) {
        this.items.addAll(lesson)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}