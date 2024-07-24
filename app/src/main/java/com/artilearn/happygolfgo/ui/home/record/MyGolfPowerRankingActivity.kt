package com.artilearn.happygolfgo.ui.home.record

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.MY_GOLF_POWER_ADAPTER_INDEX
import com.artilearn.happygolfgo.constants.PARAMS_YEAR_MONTH
import com.artilearn.happygolfgo.databinding.ActivityMyGolfPowerRankingBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGolfPowerRankingActivity :
    BaseActivity<ActivityMyGolfPowerRankingBinding>(R.layout.activity_my_golf_power_ranking) {

    private val viewModel: MyGolfPowerRankingViewModel by viewModel()
    private lateinit var rankingPagerAdapter: MyGolfPowerRankingAdapter
    var yearMonth = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yearMonth = intent.getStringExtra(PARAMS_YEAR_MONTH) ?: ""

        initBinding()
        initViewModelObserve()
        initView()
        initTabListener()
        initOnClickListener()
    }

    private fun initOnClickListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            rankingPagerAdapter =
                MyGolfPowerRankingAdapter(this@MyGolfPowerRankingActivity, yearMonth)

            binding.viewpager.adapter = rankingPagerAdapter
            TabLayoutMediator(binding.rankingListTablayout, binding.viewpager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.my_golf_power_ranking_branch_title)
                    1 -> tab.text = getString(R.string.my_golf_power_ranking_by_subject_title)
                    2 -> tab.text = getString(R.string.my_golf_power_ranking_all_branch_title)
                }
            }.attach()

            setSelectViewpager2Listener()
        }
    }

    private fun setSelectViewpager2Listener() {
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tab = binding.rankingListTablayout.getTabAt(position)
                val views = arrayListOf<View>()
                tab?.view?.findViewsWithText(views, tab.text, View.FIND_VIEWS_WITH_TEXT)
                views.forEach { view ->
                    if (view is TextView) {
                        Handler().postDelayed({
                            TextViewCompat.setTextAppearance(view, R.style.TabSelect)
                        },100)
                    }
                }
            }
        })
    }

    private fun initView() {
//        binding.myGolfPowerRakingTopSection.setOnClickListener {
//            showToast(ToastType.SUCCESS, "준비 중입니다.")
//        }
    }

    private fun initTabListener() {
    }
}
