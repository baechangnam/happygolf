package com.artilearn.happygolfgo.ui.golfgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.ItemGolfGameScoreBinding
import com.artilearn.happygolfgo.ui.golfgame.model.GolfGameModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*


class GolfGameAdapter : RecyclerView.Adapter<GolfGameAdapter.GameScoreViewHolder>() {

    lateinit var binding: ItemGolfGameScoreBinding
    private val items: MutableList<GolfGameModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameScoreViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_golf_game_score,
            parent,
            false
        )

        return GameScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameScoreViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class GameScoreViewHolder(
        private val binding: ItemGolfGameScoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GolfGameModel) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(items: List<GolfGameModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
    }

}