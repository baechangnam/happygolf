package com.artilearn.happygolfgo.ui.golfgame

import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityGolfGameBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import org.koin.androidx.viewmodel.ext.android.viewModel

class GolfGameActivity : BaseActivity<ActivityGolfGameBinding>(R.layout.activity_golf_game) {

    private val viewModel: GolfGameViewModel by viewModel()
    private lateinit var adapter: GolfGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
        initView()
        initAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@GolfGameActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@GolfGameActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@GolfGameActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.toolbar.toolbar, getString(R.string.golf_game_score_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = GolfGameAdapter()

        binding.recyclerView.adapter = adapter
    }

}