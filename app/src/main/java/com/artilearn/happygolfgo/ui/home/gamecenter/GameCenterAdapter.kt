package com.artilearn.happygolfgo.ui.home.gamecenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.ItemGamecenterRankBinding
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCRankModel

class GameCenterAdapter : RecyclerView.Adapter<GameCenterAdapter.GameCenterViewHolder>() {

    private val items: ArrayList<GCRankModel> = arrayListOf()
    lateinit var binding: ItemGamecenterRankBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCenterViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gamecenter_rank,
            parent,
            false
        )

        return GameCenterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameCenterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class GameCenterViewHolder(
        private val binding: ItemGamecenterRankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GCRankModel) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(items: List<GCRankModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

}