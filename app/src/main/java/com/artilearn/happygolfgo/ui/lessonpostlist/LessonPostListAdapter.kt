package com.artilearn.happygolfgo.ui.lessonpostlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.databinding.ItemLessonPostListBinding

class LessonPostListAdapter(
    private val itemClick: (LessonLog) -> Unit
) : RecyclerView.Adapter<LessonPostListAdapter.LessonViewHolder>() {

    lateinit var binding: ItemLessonPostListBinding
    private val items: ArrayList<LessonLog> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_lesson_post_list,
            parent,
            false
        )

        return LessonViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class LessonViewHolder(
        private val binding: ItemLessonPostListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LessonLog) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(logList: List<LessonLog>) {
        this.items.addAll(logList)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}