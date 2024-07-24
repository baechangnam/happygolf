package com.artilearn.happygolfgo.ui.home.lecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.databinding.ItemHomeLectureCommentBinding
import com.artilearn.happygolfgo.util.calculationTime
import java.text.SimpleDateFormat
import java.util.*

class LectureMyCommentAdapter(
    private val itemList: List<LectureComment>,
) : RecyclerView.Adapter<LectureMyCommentAdapter.ViewHolder>() {


    class ViewHolder(
        private val binding: ItemHomeLectureCommentBinding
    ) : BaseRecyclerViewAdapter.BaseViewHolder<LectureComment>(binding) {

        override fun bind(item: LectureComment) {
            super.bind(item)

            //binding.userName.text = item.reg_usr_id
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA)
            val oldDate: Date = dateFormat.parse(item.reg_dtm)
            val oldMillis = oldDate.time

            binding.regDate.text = calculationTime(oldMillis)
            binding.contents.text = item.cmmt_text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeLectureCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

    }


}


