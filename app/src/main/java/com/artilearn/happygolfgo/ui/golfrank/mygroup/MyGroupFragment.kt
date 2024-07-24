package com.artilearn.happygolfgo.ui.golfrank.mygroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentMygroupBinding
import com.artilearn.happygolfgo.ui.golfrank.GolfRankAdapter
import com.artilearn.happygolfgo.ui.golfrank.GolfRankViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyGroupFragment : BaseFragment<FragmentMygroupBinding, GolfRankViewModel>(R.layout.fragment_mygroup) {

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

        binding.rvMyGroup.adapter = adapter
    }

}