package com.artilearn.happygolfgo.ui.lessonpostlist

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.databinding.ActivityLessonPostListBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.log.LogActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class LessonPostListActivity
    : BaseActivity<ActivityLessonPostListBinding>(R.layout.activity_lesson_post_list) {

    private val viewModel: LessonPostListViewModel by viewModel()
    private lateinit var adapter: LessonPostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@LessonPostListActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@LessonPostListActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            appRefresh.observe(this@LessonPostListActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.lessonPostListToolbar.toolbar, getString(R.string.lesson_post_list_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = LessonPostListAdapter { log ->
            goLog(log)
        }

        binding.recyclerview.adapter = adapter
    }

    private fun goLog(log: LessonLog) {
        val logIntent = Intent(this, LogActivity::class.java)
            .apply {
                putExtra(LESSON_ID, log.id)
            }
        startActivity(logIntent)
    }
}