package com.artilearn.happygolfgo.ui.home.record

import androidx.fragment.app.Fragment

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyGolfPowerPagerAdapter(
    private val fa: FragmentActivity,
    private var itemCount: Int = 1
) : FragmentStateAdapter(fa) {

    override fun getItemCount() = itemCount

    fun reloadMyGolfPagerAdapter(itemCount : Int  = 1)    {
        this.itemCount = itemCount
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyGolfPowerFragment.newInstance(position)
            1 -> MyGolfPowerFragment.newInstance(position)
            else -> MyGolfPowerFragment.newInstance(position)
        }
    }
}
