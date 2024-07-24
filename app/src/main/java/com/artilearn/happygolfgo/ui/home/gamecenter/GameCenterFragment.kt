package com.artilearn.happygolfgo.ui.home.gamecenter

import android.content.Intent
import android.graphics.Color
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentGamecenterBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.golfrank.GolfRankActivity
import com.artilearn.happygolfgo.ui.golfgame.GolfGameActivity
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCAveragesModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameCenterFragment :
    BaseFragment<FragmentGamecenterBinding, GameCenterViewModel>(R.layout.fragment_gamecenter) {
//    private val TAG = javaClass.simpleName
    override val viewModel: GameCenterViewModel by viewModel()
    private lateinit var adapter: GameCenterAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
        initBarChart()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            button.observe(this@GameCenterFragment) {
                when (button.value) {
                    GameCenterViewModel.ButtonType.RANK -> goGameRank()
                    GameCenterViewModel.ButtonType.GAME -> goGameScore()
                    else -> goGameRank() //when must be exhaustive
                }
            }
            user.observe(this@GameCenterFragment){ user ->
                binding.user = user
            }
            averages.observe(this@GameCenterFragment) { averages ->
                binding.averages = averages
                setBarData(averages)
            }
            sumary.observe(this@GameCenterFragment) { sumary ->
                binding.sumary = sumary
            }
            networkFail.observe(this@GameCenterFragment) {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            }
            serverError.observe(this@GameCenterFragment) {
                showFragmentToast(ToastType.ERROR, it)
            }
            appRefresh.observe(this@GameCenterFragment) {
                goRefreshFromFragment()
            }
        }
    }

    private fun initAdapter() {
        adapter = GameCenterAdapter()

        binding.rvAllRank.adapter = adapter
    }

    private fun initBarChart() {
        with(binding) {
            barChart.run {
                setDrawBarShadow(true)
                setTouchEnabled(false)
                setDrawValueAboveBar(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                description.isEnabled = false
                legend.isEnabled = false
                setVisibleXRangeMaximum(5f)

                xAxis.run {
                    isEnabled = false
                    setDrawGridLines(false)
                    granularity = 1f
                    setDrawAxisLine(false)
                    textSize = 12f
                }

                axisLeft.run {
                    isEnabled = false
                    axisMinimum = 0f
                    axisMaximum = 100f
                }
                axisRight.run {
                    isEnabled = false
                }

                animateY(1000)
                animateX(700)
            }
        }
    }

    private fun setBarData(item: GCAveragesModel) {
        val values = mutableListOf<BarEntry>()
        val colorList = listOf(
            Color.rgb(204, 93, 232),
            Color.rgb(132, 94, 247),
            Color.rgb(92, 124, 250),
            Color.rgb(34, 184, 207),
            Color.rgb(81, 207, 102)
        )

        for (i in 0..4) {
            when (i) {
                0 -> values.add(BarEntry(i.toFloat(), item.driver.toFloat()))
                1 -> values.add(BarEntry(i.toFloat(), item.woodUtil.toFloat()))
                2 -> values.add(BarEntry(i.toFloat(), item.iron.toFloat()))
                3 -> values.add(BarEntry(i.toFloat(), item.shortGame.toFloat()))
                4 -> values.add(BarEntry(i.toFloat(), item.putter.toFloat()))
            }
        }

        val set = BarDataSet(values, "내용없음")
            .apply {
                setDrawIcons(false)
                setDrawValues(false)
                colors = colorList
                valueTextColor = Color.RED
            }

        val dataSets = mutableListOf<IBarDataSet>()
        dataSets.add(set)

        val data = BarData(dataSets)
            .apply {
                setValueTextSize(10f)
                barWidth = 0.5f
            }

        binding.barChart.data = data
    }

    private fun goGameRank() {
        val rankIntent = Intent(activity, GolfRankActivity::class.java)
        startActivity(rankIntent)
    }

    private fun goGameScore() {
        val scoreIntent = Intent(activity, GolfGameActivity::class.java)
        startActivity(scoreIntent)
    }

}