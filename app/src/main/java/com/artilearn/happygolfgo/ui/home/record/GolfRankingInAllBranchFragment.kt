package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.MY_GOLF_POWER_ADAPTER_INDEX
import com.artilearn.happygolfgo.constants.PARAMS_YEAR_MONTH
import com.artilearn.happygolfgo.databinding.FragmentGolfRankingInAllBranchBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import org.koin.androidx.viewmodel.ext.android.viewModel

class GolfRankingInAllBranchFragment :
    BaseFragment<FragmentGolfRankingInAllBranchBinding, GolfRankingInAllBranchViewModel>
    (R.layout.fragment_golf_ranking_in_all_branch){
    private var yearMonth = ""
    override val viewModel: GolfRankingInAllBranchViewModel by viewModel()

    private lateinit var  adapter: GolfRankingInAllBranchAdapter

    companion object {
        fun newInstance(yearMonth: String): GolfRankingInAllBranchFragment {
            val args = Bundle()
            args.putString(PARAMS_YEAR_MONTH, yearMonth)
            val fragment = GolfRankingInAllBranchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        initBinding()
        initAdapter()
        initViewModelObserving()
        initRequest()
    }

    private fun initBinding(){
        binding.vm = viewModel
    }

    private fun initAdapter() {
        adapter = GolfRankingInAllBranchAdapter(viewModel)
        binding.recyclerview.adapter = adapter

        yearMonth = arguments?.getString(PARAMS_YEAR_MONTH) ?: ""
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@GolfRankingInAllBranchFragment) {
                showFragmentToast(ToastType.ERROR, it)
            }
            networkFail.observe(this@GolfRankingInAllBranchFragment) {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            }
        }
    }

    private fun initRequest() {
        viewModel.yearMonth = yearMonth
        viewModel.request()
    }
}