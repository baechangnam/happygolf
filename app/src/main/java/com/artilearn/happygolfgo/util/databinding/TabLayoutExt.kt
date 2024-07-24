package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import com.google.android.material.tabs.TabLayout

@BindingAdapter("bindTabLayout")
fun TabLayout.tabItemClick(vm: TicketManagerViewModel) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.position?.let { position  ->
                vm.tabItemClick(position)
            } ?: vm.tabItemClick(100)
        }
    })
}
