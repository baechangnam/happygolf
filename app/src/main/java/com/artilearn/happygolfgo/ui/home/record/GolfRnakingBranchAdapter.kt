package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInBranchAdapterBinding
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInBranchAdapterNormalBinding
import com.artilearn.happygolfgo.ui.home.record.model.RankingInBranchData

class GolfRankingInBranchAdapter(
   private val golfRankingInBranchViewModel : GolfRankingInBranchViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private  val items = arrayListOf<RankingInBranchData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 ->  {
                GolfRankingInMyBranchHeaderViewHolder.create(parent)
            }
            else -> {
                GolfRankingNormalInMyBranchHolder.create(parent)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val  ranking  = items[position]
        when( position) {
             0  ->  {
                 ( holder as  GolfRankingInMyBranchHeaderViewHolder).bind(ranking, golfRankingInBranchViewModel.title)
             }
            else ->  {
                ( holder as  GolfRankingNormalInMyBranchHolder).bind(ranking)
            }
        }
    }

    class GolfRankingInMyBranchHeaderViewHolder(private val binding: ItemGolfRankingInBranchAdapterBinding)
        : RecyclerView.ViewHolder(binding.root) {
             fun bind(item:RankingInBranchData, branchName:String) {
                  binding.ranking = item
                  binding.branchName = branchName
                  binding.executePendingBindings()
             }
            companion object GolfRankingInMyBranchHeaderViewHolderFactory {
                fun create(parent:ViewGroup) :  GolfRankingInMyBranchHeaderViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val view = ItemGolfRankingInBranchAdapterBinding.inflate(layoutInflater, parent, false)
                    return GolfRankingInMyBranchHeaderViewHolder(view)
                }
            }
        }

    class GolfRankingNormalInMyBranchHolder(private val binding: ItemGolfRankingInBranchAdapterNormalBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:RankingInBranchData) {
                  binding.ranking = item
                  binding.executePendingBindings()
        }
        companion object GolfRankingNormalInMyBranchHolderFactory {
            fun create(parent:ViewGroup) :  GolfRankingNormalInMyBranchHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemGolfRankingInBranchAdapterNormalBinding.inflate(layoutInflater, parent, false)
                return GolfRankingNormalInMyBranchHolder(view)
            }
        }
    }

    fun setItems(rankings: List<RankingInBranchData>) {
        this.items.addAll(rankings)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

}