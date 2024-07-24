package com.artilearn.happygolfgo.ui.home.lecture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.data.LectureHome
import com.artilearn.happygolfgo.databinding.ItemHomeLectureBinding
import com.artilearn.happygolfgo.databinding.ItemHomeLectureListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_home_lecture.view.*


var itemClick: LectureFragment.ItemClick? = null
class LectureListAdapter(private var bookmarkClick: LectureFragment.ItemClick) : BaseRecyclerViewAdapter<LectureHome>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<LectureHome> {
        itemClick = bookmarkClick
        return LectureViewHolder(
            ItemHomeLectureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class LectureViewHolder(
        private val binding: ItemHomeLectureBinding
    ) : BaseViewHolder<LectureHome>(binding) {
        override fun bind(item: LectureHome) {
            super.bind(item)

            binding.viewAllBtn.setOnClickListener {
                itemClick?.itemClick(adapterPosition , "viewAll",0 , adapterPosition)
            }
            binding.viewAll.setOnClickListener {
                itemClick?.itemClick(adapterPosition , "viewAll",0 , adapterPosition)
            }

            binding.root.list_training.apply {
                adapter = itemClick?.let { TrainingListAdapter(item.lectures, adapterPosition) }
                layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }

        }

    }
}

class TrainingListAdapter(private val itemList: List<Lecture>, private val parentPos: Int) :
    RecyclerView.Adapter<TrainingListAdapter.TrainingListViewHolder>() {

    class TrainingListViewHolder(
        private val binding: ItemHomeLectureListBinding, override val parentPoss: Int
    ) : BaseRecyclerViewAdapter.ChildBaseViewHolder<Lecture>(binding) {
        override fun bind(item: Lecture) {
            super.bind(item)

            binding.tvTitle.text = item.lect_title
            binding.tvHeart.text = item.like_cnt
            binding.tvView.text = item.view_cnt

            Glide.with(binding.root.context)
                .load(item.thmn_url)
                .apply(
                    RequestOptions.bitmapTransform(
                    MultiTransformation(
                        RoundedCorners(32)
                    )
                ))
                .into(binding.ivThumbnail)

            if(item.like_yn=="Y"){
                binding.llFavor.setBackgroundResource(R.drawable.bookmark_bg_on)
                binding.llFavorIcon.setBackgroundResource(R.drawable.favor_ons)
                binding.llFavorTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_reservation_plate_text_day))

            }else{
                binding.llFavor.setBackgroundResource(R.drawable.bookmark_bg)
                binding.llFavorIcon.setBackgroundResource(R.drawable.favor)
                binding.llFavorTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))
            }

            if(item.watch_yn=="Y") {
                binding.ivPlayEnd.visibility = View.VISIBLE
            }else{
                binding.ivPlayEnd.visibility = View.GONE
            }




            binding.llFavor.setOnClickListener {
                itemClick?.itemClick(adapterPosition , "bookmark",0 , parentPoss)
            }

            binding.root.setOnClickListener {
                itemClick?.itemClick(adapterPosition , "detail",0,parentPoss)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingListViewHolder {
        val binding =
            ItemHomeLectureListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingListViewHolder(binding ,parentPos)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TrainingListViewHolder, position: Int) {
        holder.bind(itemList[position])

    }
}


