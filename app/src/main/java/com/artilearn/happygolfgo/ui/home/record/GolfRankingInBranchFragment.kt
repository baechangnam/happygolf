package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.MY_GOLF_POWER_ADAPTER_INDEX
import com.artilearn.happygolfgo.databinding.FragmentGolfRankingInBranchBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.constants.PARAMS_YEAR_MONTH
import org.koin.androidx.viewmodel.ext.android.viewModel

class GolfRankingInBranchFragment :
    BaseFragment<FragmentGolfRankingInBranchBinding, GolfRankingInBranchViewModel>
    (R.layout.fragment_golf_ranking_in_branch){
    private var yearMonth = ""
    override val viewModel: GolfRankingInBranchViewModel by viewModel()
    private lateinit var  adapter: GolfRankingInBranchAdapter

    companion object {
        fun newInstance(yearMonth: String): GolfRankingInBranchFragment {
            val args = Bundle()
            args.putString(PARAMS_YEAR_MONTH, yearMonth)
            val fragment = GolfRankingInBranchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        initBinding()
        initAdapter()
        yearMonth = arguments?.getString(PARAMS_YEAR_MONTH) ?: ""
        initViewModelObserving()
        initRequest()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@GolfRankingInBranchFragment) {
                showFragmentToast(ToastType.ERROR, it)
            }
            networkFail.observe(this@GolfRankingInBranchFragment) {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            }
        }
    }

    private fun initAdapter() {
        adapter = GolfRankingInBranchAdapter(viewModel)
        binding.recyclerview.adapter = adapter
    }

    private fun initRequest() {
        viewModel.yearMonth = yearMonth
        viewModel.request()
    }
}