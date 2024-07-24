package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInAllBranchAdapterBinding
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInAllBranchAdapterNormalBinding
import com.artilearn.happygolfgo.ui.home.record.model.RankingInAllBranchData


class GolfRankingInAllBranchAdapter(
    private val golfRankingInAllBranchViewModel : GolfRankingInAllBranchViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private  val items =  arrayListOf<RankingInAllBranchData>()
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
//        return super.getItemViewType(position)
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ranking = items[position]
        when ( position) {
            0  ->  {
                ( holder as  HeaderViewHolder).bind(ranking, golfRankingInAllBranchViewModel.title)
            }
            else ->  {
                ( holder as  NormalViewHolder).bind(ranking)
            }
        }
    }

    class HeaderViewHolder(private val binding: ItemGolfRankingInAllBranchAdapterBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(ranking:RankingInAllBranchData, titleAllBranch:String) {
            binding.titleAllBranch = titleAllBranch;
            binding.ranking =  ranking
            binding.executePendingBindings()
        }
        companion object HeaderViewHolderFactory {
            fun create(parent: ViewGroup) :  HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemGolfRankingInAllBranchAdapterBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    class NormalViewHolder(private val binding: ItemGolfRankingInAllBranchAdapterNormalBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(ranking:RankingInAllBranchData) {
                  binding.ranking = ranking
                  binding.executePendingBindings()
        }
        companion object NormalViewHolderFactory {
            fun create(parent: ViewGroup) :  NormalViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemGolfRankingInAllBranchAdapterNormalBinding.inflate(layoutInflater, parent, false)
                return NormalViewHolder(view)
            }
        }
    }

    fun setItems(rankings: List<RankingInAllBranchData>) {
        this.items.addAll(rankings)
        notifyDataSetChanged()
    }

    fun clear() {
         this.items.clear()
        notifyDataSetChanged()
    }
}