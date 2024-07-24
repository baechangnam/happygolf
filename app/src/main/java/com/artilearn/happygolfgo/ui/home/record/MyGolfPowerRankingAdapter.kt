package com.artilearn.happygolfgo.ui.home.record

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyGolfPowerRankingAdapter (
    private val fa: FragmentActivity,
    private val yearMonth: String,
) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->  GolfRankingInBranchFragment.newInstance(yearMonth)
            1 -> GolfRankingBySubjectFragment.newInstance(yearMonth)
            2 -> GolfRankingInAllBranchFragment.newInstance(yearMonth)
            else ->  GolfRankingInBranchFragment.newInstance(yearMonth)
        }
    }
}