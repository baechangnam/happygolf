package com.artilearn.happygolfgo.ui.golfrank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.databinding.ItemGroupRankBinding
import com.artilearn.happygolfgo.ui.golfrank.model.GolfRankModel

class GolfRankAdapter() : RecyclerView.Adapter<GolfRankAdapter.GameRankHolder>() {

    lateinit var binding: ItemGroupRankBinding
    private val items: MutableList<GolfRankModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRankHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_group_rank,
            parent,
            false
        )

        return GameRankHolder(binding).also {

        }
    }

    override fun onBindViewHolder(holder: GameRankHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class GameRankHolder(
        private val binding: ItemGroupRankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GolfRankModel) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(items: List<GolfRankModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}