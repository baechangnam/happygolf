package com.artilearn.happygolfgo.ui.home.lecture

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentLectureListBinding
import com.artilearn.happygolfgo.enummodel.ToastType

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureFragment
    : BaseFragment<FragmentLectureListBinding, LectureFragmentViewModel>(R.layout.fragment_lecture_list) {

    override val viewModel: LectureFragmentViewModel by viewModel()
    private lateinit var adapter: LectureListAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter();
    }

    private fun initBinding() {
        binding.vm = viewModel
        binding.etKeyword.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val detailActivity = Intent(activity, LectureSearchActivity::class.java)
                        .apply {
                            putExtra("keyword", binding.etKeyword.text.toString())
                        }
                    startActivity(detailActivity)
                    return true
                }
                return false
            }
        })
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@LectureFragment, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@LectureFragment, Observer {
                showFragmentToast(
                    ToastType.WARNNING,
                    getString(R.string.fragment_network_fail_content)
                )
            })

            addBookMark.observe(this@LectureFragment, Observer {
                if(it){
                    Log.d("myLog", "addBookMark ok ");
                }
            })

        }
    }


    private fun initAdapter() {
        var itemClick = ItemClick()
        adapter = LectureListAdapter(itemClick)

        with(binding) {
            recyclerview.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            // swipeToDelete(recyclerview)
            recyclerview.adapter = adapter
        }
    }

    inner class ItemClick(){
        fun itemClick(pos : Int, tag : String, count : Int, parentpos : Int) {
            Log.d("myLog", "parentpos " +parentpos);
            when (tag) {
                "bookmark" -> {
                    val currentList = adapter.currentList.toMutableList()
                    val lectNo =  currentList[parentpos].lectures[pos].lect_no
                    val favor_yn =  currentList[parentpos].lectures[pos].like_yn
                    if(favor_yn=="Y"){
                        binding.vm?.cancelLike(lectNo)
                        currentList[parentpos].lectures[pos].like_yn= "N"
                    }else{
                        binding.vm?.addLike(lectNo)
                        currentList[parentpos].lectures[pos].like_yn= "Y"
                    }
                    adapter.notifyDataSetChanged()


                    //adapter.notifyDataSetChanged()
                }
                "viewAll" -> {
                    val currentList = adapter.currentList.toMutableList()
                    val sectNo =  currentList[parentpos].sect_no
                    val sectTitle =  currentList[parentpos].sect_title

                    val detailActivity = Intent(activity, LectureSectionActivity::class.java)
                        .apply {
                            putExtra("sectNo", sectNo)
                            putExtra("sectTitle", sectTitle)
                        }
                    startActivity(detailActivity)
                }
                else -> {
                    val currentList = adapter.currentList.toMutableList()
                    val lectNo =  currentList[parentpos].lectures[pos].lect_no
                    val lectUrl =  currentList[parentpos].lectures[pos].rsc_url

                    val detailActivity = Intent(activity, LectureDetailActivity::class.java)
                        .apply {
                            putExtra("lectNo", lectNo)
                            putExtra("lectUrl", lectUrl)
                        }
                    startActivity(detailActivity)
                }
            }


        }
    }




}