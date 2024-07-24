package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPower
import com.artilearn.happygolfgo.databinding.ItemScoreBinding

class ScoreAdapter (
    private val itemClick: (GolfPower) -> Unit
)   : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>()              {
    lateinit var binding: ItemScoreBinding
    private val items: ArrayList<GolfPower> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_score,
            parent,
            false
        )

        return ScoreViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ScoreViewHolder(
        private val binding: ItemScoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GolfPower) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(swingVideo: List<GolfPower>) {
        this.items.addAll(swingVideo)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

}