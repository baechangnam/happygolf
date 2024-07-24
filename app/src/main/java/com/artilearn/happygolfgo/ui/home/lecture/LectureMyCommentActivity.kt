package com.artilearn.happygolfgo.ui.home.lecture

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.databinding.ActivityLectureMyCommentBinding
import com.artilearn.happygolfgo.databinding.ActivityLectureMylikeBinding
import com.artilearn.happygolfgo.databinding.ActivityLectureMypickBinding
import com.artilearn.happygolfgo.databinding.ActivityLectureSearchBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureMyCommentActivity :
    BaseActivity<ActivityLectureMyCommentBinding>(R.layout.activity_lecture_my_comment) {

    private val viewModel: LectureMyCommentListViewModel by viewModel()
    private lateinit var adapter: LectureMyCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()

        binding.vm?.getCommentMy()

    }

    private fun initBinding() {
        binding.vm = viewModel
        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            commentData.observe(this@LectureMyCommentActivity, Observer {
                Log.d("myLog", "comment " + it!!.size)
                setComment(it)
            })


        }
    }

    private lateinit var commentList : List<LectureComment>


    private fun setComment(lectureComment : List<LectureComment>?) {
        if (lectureComment != null) {
            commentList = lectureComment
        }
        adapter = commentList?.let { LectureMyCommentAdapter(it) }!!
        val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.rvComment.layoutManager = layoutManager


        with(binding) {
            binding.rvComment.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            // swipeToDelete(recyclerview)
            binding.rvComment.adapter = adapter
        }

        val color = getColor(R.color.color_lect_green) // 변경하려는 색상
        val str1 = "총 "
        val str2 = commentList.size.toString()+"개"
        val str3 = "의 댓글"
        val spannable = SpannableString("$str1$str2$str3")
        spannable.setSpan(ForegroundColorSpan(color), str1.length, str1.length+str2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.sectCount.setText(spannable, TextView.BufferType.SPANNABLE)


        if(commentList.isEmpty()){
            binding.noList.visibility=View.VISIBLE
        }else{
            binding.noList.visibility=View.GONE
        }
    }







}