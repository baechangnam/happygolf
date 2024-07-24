package com.artilearn.happygolfgo.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.banner.Banner
import com.artilearn.happygolfgo.databinding.ItemBannerBinding
import com.artilearn.happygolfgo.databinding.ItemSwingVideoListBinding
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.util.databinding.bindImage
import com.bumptech.glide.Glide

class BannerViewPagerAdapter (
    private val itemClick: (Banner) -> Unit
)   : RecyclerView.Adapter<BannerViewPagerAdapter.BannerViewHolder>()              {
    lateinit var binding: ItemBannerBinding
    private val items: ArrayList<Banner> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_banner,
            parent,
            false
        )

        return BannerViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class BannerViewHolder(
        private val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Banner) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(binding.ivBanner)
        }
    }

    fun setItems(banners: List<Banner>) {
        this.items.addAll(banners)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

}