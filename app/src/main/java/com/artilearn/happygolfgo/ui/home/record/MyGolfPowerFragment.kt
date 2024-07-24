package com.artilearn.happygolfgo.ui.home.record

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.MY_GOLF_POWER_ADAPTER_INDEX
import com.artilearn.happygolfgo.databinding.FragmentMyGolfPowerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGolfPowerFragment : BaseFragment<FragmentMyGolfPowerBinding, MyGolfPowerViewModel>
    (R.layout.fragment_my_golf_power) {
    private var pageIndex = -1
    override val viewModel: MyGolfPowerViewModel by viewModel()

    companion object {
        fun newInstance(pageIndex: Int): MyGolfPowerFragment {
            val args = Bundle()
            args.putInt(MY_GOLF_POWER_ADAPTER_INDEX, pageIndex)
            val fragment = MyGolfPowerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        val pageIndex = arguments?.getInt(MY_GOLF_POWER_ADAPTER_INDEX)
        pageIndex?.let {
            this.pageIndex = pageIndex
        }
        viewModel.pageIndex = pageIndex
        binding.vm = viewModel
        initBinding()
        initViewEvent()
        initViewModelObserving()
        initRequest()
    }

    private fun initBinding() {
    }

    private fun initViewModelObserving() {

    }

    private fun launchMyGolfPowerRankingActivity() {
        if (pageIndex > -1) {
            val myGolfPowerRankingActivityIntent =
                Intent(activity, MyGolfPowerRankingActivity::class.java)
                    .apply {
                        putExtra(MY_GOLF_POWER_ADAPTER_INDEX, pageIndex)
                    }
            startActivity(myGolfPowerRankingActivityIntent)
        }
    }

    private fun popupRankingInfoDialog() {
        RankingInfoDialog(requireContext()) {

        }.show()
    }

    private fun initViewEvent() {
        this.binding.btnLaunchBranchRanking.setOnClickListener {
            launchMyGolfPowerRankingActivity()
        }
        this.binding.llNotOpenTopTitle.setOnClickListener {
            popupRankingInfoDialog()
        }
        this.binding.llOpenTopTitle.setOnClickListener {
            popupRankingInfoDialog()
        }
    }

    private fun initRequest() {
        viewModel.request()
    }
}