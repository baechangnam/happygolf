package com.artilearn.happygolfgo.ui.home.lecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.databinding.ItemSectLectureListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class LectureMyLikeAdapter(
    private val itemList: List<Lecture>,
    private val lectureItemClicks: LectureMyLikeActivity.LectureItemClick
) : RecyclerView.Adapter<LectureMyLikeAdapter.TrainingListViewHolder>() {

    class TrainingListViewHolder(
        private val binding: ItemSectLectureListBinding, override val lectureItemClick: LectureMyLikeActivity.LectureItemClick,
    ) : BaseRecyclerViewAdapter.ClickViewHolder<Lecture>(binding) {

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

            if(item.favr_yn=="Y"){
                binding.ivHeart.setBackgroundResource(R.drawable.btn_like_on)
            }else{
                binding.ivHeart.setBackgroundResource(R.drawable.heart_off)
            }

//            binding.llFavor.setOnClickListener {
//                lectureItemClick?.itemClick(adapterPosition , "bookmark")
//            }

            binding.root.setOnClickListener {
                lectureItemClick?.itemClick(adapterPosition , "detail")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingListViewHolder {
        val binding =
            ItemSectLectureListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrainingListViewHolder(binding,lectureItemClicks)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TrainingListViewHolder, position: Int) {
        holder.bind(itemList[position])

    }
}


