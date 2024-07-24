package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.databinding.ItemLessonPostListBinding
import com.artilearn.happygolfgo.databinding.ItemSwingVideoListBinding
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListAdapter

//TRSummarySwingVideoRecordModel
class SwingVideoListAdapter (
    private val itemClick: (TRSummarySwingVideoRecordModel) -> Unit
)   : RecyclerView.Adapter<SwingVideoListAdapter.SwingVideoViewHolder>()              {
    lateinit var binding: ItemSwingVideoListBinding
    private val items: ArrayList<TRSummarySwingVideoRecordModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwingVideoViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_swing_video_list,
            parent,
            false
        )

        return SwingVideoViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: SwingVideoListAdapter.SwingVideoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class SwingVideoViewHolder(
        private val binding: ItemSwingVideoListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TRSummarySwingVideoRecordModel) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(swingVideo: List<TRSummarySwingVideoRecordModel>) {
        this.items.addAll(swingVideo)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

}