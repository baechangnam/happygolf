package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInSubjectAdapterBinding
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInSubjectAdapterNormalBinding
import com.artilearn.happygolfgo.ui.home.record.model.RankingBySubjectData

class GolfRankingBySubjectAdapter(
    private val golfRankingBySubjectViewModel : GolfRankingBySubjectViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private  val items = arrayListOf<RankingBySubjectData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 ->  {
                HeaderViewHolder.create(parent)
            }
            else -> {
                NormalViewHolder.create(parent)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val  ranking = items[position]
        when( position) {
            0  ->  {
                ( holder as HeaderViewHolder).bind(ranking)
            }
            else ->  {
                ( holder as NormalViewHolder).bind(ranking)
            }
        }
    }

    class HeaderViewHolder(private val binding: ItemGolfRankingInSubjectAdapterBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(ranking:RankingBySubjectData) {
                  binding.ranking = ranking
                  binding.executePendingBindings()
        }
        companion object HeaderViewHolderFactory {
            fun create(parent: ViewGroup) : HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemGolfRankingInSubjectAdapterBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    class NormalViewHolder(private val binding: ItemGolfRankingInSubjectAdapterNormalBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(ranking:RankingBySubjectData) {
            binding.ranking = ranking
            binding.executePendingBindings()
        }
        companion object GolfRankingNormalInMyBranchHolderFactory {
            fun create(parent: ViewGroup) : NormalViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemGolfRankingInSubjectAdapterNormalBinding.inflate(layoutInflater, parent, false)
                return NormalViewHolder(view)
            }
        }
    }

    fun setItems(rankings: List<RankingBySubjectData>) {
        this.items.addAll(rankings)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}