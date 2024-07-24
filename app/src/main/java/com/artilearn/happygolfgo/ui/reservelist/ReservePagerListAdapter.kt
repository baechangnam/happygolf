package com.artilearn.happygolfgo.ui.reservelist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.ui.reservelist.fragment.ReserveLessonFragment
import com.artilearn.happygolfgo.ui.reservelist.fragment.ReservePlateFragment

class ReservePagerListAdapter(
    private val fa: FragmentActivity,
    private val response: ReserveListResponse
) : FragmentStateAdapter(fa) {


    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservePlateFragment.newInstance(response.data.plateReservations)
            1 -> ReserveLessonFragment.newInstance(response.data.lessonReservations)
            else -> ReservePlateFragment.newInstance(response.data.plateReservations)
        }
    }

}
