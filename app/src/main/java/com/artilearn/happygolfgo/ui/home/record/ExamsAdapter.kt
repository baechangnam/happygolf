package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.databinding.ItemExamBinding
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInAllBranchAdapterBinding
import com.artilearn.happygolfgo.databinding.ItemGolfRankingInAllBranchAdapterNormalBinding
import com.artilearn.happygolfgo.ui.home.record.model.RankingInAllBranchData


class ExamsAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private  val items =  arrayListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ranking = items[position]
        ( holder as  NormalViewHolder).bind(ranking)
    }

    class NormalViewHolder(private val binding: ItemExamBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(ranking:String) {
            binding.tvExam.text = ranking
        }
        companion object NormalViewHolderFactory {
            fun create(parent: ViewGroup) :  NormalViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemExamBinding.inflate(layoutInflater, parent, false)
                return NormalViewHolder(view)
            }
        }
    }

    fun setItems(rankings: List<String>) {
        this.items.addAll(rankings)
        notifyDataSetChanged()
    }

    fun clear() {
         this.items.clear()
        notifyDataSetChanged()
    }
}