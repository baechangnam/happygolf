package com.artilearn.happygolfgo.ui.golfrank.allgroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentAllgroupBinding
import com.artilearn.happygolfgo.ui.golfrank.GolfRankAdapter
import com.artilearn.happygolfgo.ui.golfrank.GolfRankViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AllGroupFragment : BaseFragment<FragmentAllgroupBinding, GolfRankViewModel>(R.layout.fragment_allgroup) {

    override val viewModel: GolfRankViewModel by sharedViewModel()
    private lateinit var adapter: GolfRankAdapter

    override fun init() {

        initBinding()
        initAdapter()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun initAdapter() {
        adapter = GolfRankAdapter()

        binding.rvAllGroup.adapter = adapter
    }

}