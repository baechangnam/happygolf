package com.artilearn.happygolfgo.ui.golfrank

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artilearn.happygolfgo.ui.golfrank.allgroup.AllGroupFragment
import com.artilearn.happygolfgo.ui.golfrank.mygroup.MyGroupFragment

const val ALL_GROUP = 0
const val MY_GROUP = 1

class GameRankPagerAdapter(
    private val fa: FragmentActivity
) : FragmentStateAdapter(fa) {

    private val tabFragmentsCreate: Map<Int, () -> Fragment> = mapOf(
        ALL_GROUP to { AllGroupFragment() },
        MY_GROUP to { MyGroupFragment() }
    )

    override fun getItemCount() = tabFragmentsCreate.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreate[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}