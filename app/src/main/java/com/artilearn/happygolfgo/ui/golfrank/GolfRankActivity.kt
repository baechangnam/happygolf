package com.artilearn.happygolfgo.ui.golfrank

import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityGolfRankBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class GolfRankActivity : BaseActivity<ActivityGolfRankBinding>(R.layout.activity_golf_rank) {

    private val viewModel: GolfRankViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
        initView()
        initPagerAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@GolfRankActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@GolfRankActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@GolfRankActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.rankToolbar.toolbar, getString(R.string.game_rank_toolbar_title), true)
    }

    private fun initPagerAdapter() {
        with(binding) {
            viewpager.adapter = GameRankPagerAdapter(this@GolfRankActivity)

            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = getTabTitle(position)
            }.attach()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            ALL_GROUP -> getString(R.string.game_rank_tab_title_allgroup)
            MY_GROUP -> getString(R.string.game_rank_tab_title_mygroup)
            else -> throw IndexOutOfBoundsException()
        }
    }

}