package com.artilearn.happygolfgo.ui.home.lecture

import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.databinding.ActivityLectureSearchBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureSearchActivity :
    BaseActivity<ActivityLectureSearchBinding>(R.layout.activity_lecture_search) {

    private val viewModel: LectureSectionViewModel by viewModel()
    private val keyword by lazy { intent.getStringExtra("keyword") }
    private lateinit var adapter: LectureSearchAdapter
    private lateinit var lectureList: List<Lecture>
    private  var orderType : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()

        binding.vm?.getLectureList(keyword!!, "","",orderType)

    }

    private fun initBinding() {
        binding.vm = viewModel
        binding.sectTitles.text = "'"+keyword!!+"'" +" 검색 결과"
        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            _lecture.observe(this@LectureSearchActivity , Observer{
                initAdapter(it!!.lectureList)
            })

            networkFail.observe(this@LectureSearchActivity, Observer {
                showNetworkFail()
            })
            serverError.observe(this@LectureSearchActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            appRefresh.observe(this@LectureSearchActivity, Observer {
                goRefresh()
            })
        }
    }


    private fun initAdapter(lectList : List<Lecture>) {
        val color = getColor(R.color.color_lect_green) // 변경하려는 색상
        val str1 = "총 "
        val str2 = lectList.size.toString()+"개"
        val str3 = "의 강의"
        val spannable = SpannableString("$str1$str2$str3")
        spannable.setSpan(ForegroundColorSpan(color), str1.length, str1.length+str2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.sectCount.setText(spannable, TextView.BufferType.SPANNABLE)

        lectureList = lectList

        if(lectList.isEmpty()){
            binding.noList.visibility=View.VISIBLE
        }else{
            binding.noList.visibility=View.GONE
        }


        adapter = LectureSearchAdapter(lectureList,LectureItemClick())
        val layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.listLectSect.layoutManager = layoutManager
        binding.listLectSect.addItemDecoration(
            GridSpacingItemDecoration(spanCount = 2, spacing = 15f.fromDpToPx())
        )

        with(binding) {
            binding.listLectSect.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            // swipeToDelete(recyclerview)
            binding.listLectSect.adapter = adapter
        }
    }

    fun Float.fromDpToPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

    inner class LectureItemClick(){
        fun itemClick(pos : Int, tag : String) {

            when (tag) {
                "bookmark" -> {
                    val lectNo =  lectureList[pos].lect_no
                    val favor_yn =  lectureList[pos].like_yn
                    if(favor_yn=="Y"){
                        binding.vm?.cancelLike(lectNo)
                        lectureList[pos].like_yn= "N"
                    }else{
                        binding.vm?.addLike(lectNo)
                        lectureList[pos].like_yn= "Y"
                    }
                    adapter.notifyDataSetChanged()

                }
                else -> {
                    val lectNo =  lectureList[pos].lect_no

                    val lectUrl =  lectureList[pos].rsc_url

                    val detailActivity = Intent(baseContext, LectureDetailActivity::class.java)
                        .apply {
                            putExtra("lectNo", lectNo)
                            putExtra("lectUrl", lectUrl)

                        }
                    startActivity(detailActivity)
                }
            }


        }
    }

    internal class GridSpacingItemDecoration(
        private val spanCount: Int, // Grid의 column 수
        private val spacing: Int // 간격
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position: Int = parent.getChildAdapterPosition(view)

            if (position >= 0) {
                val column = position % spanCount // item column
                outRect.apply {
                    // spacing - column * ((1f / spanCount) * spacing)
                    left = spacing - column * spacing / spanCount
                    // (column + 1) * ((1f / spanCount) * spacing)
                    right = (column + 1) * spacing / spanCount
                    if (position < spanCount) top = spacing
                    bottom = spacing
                }
            } else {
                outRect.apply {
                    left = 0
                    right = 0
                    top = 0
                    bottom = 0
                }
            }
        }
    }



}