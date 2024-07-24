package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("onRefresh")
fun SwipeRefreshLayout.bindOnRefresh(listener: SwipeRefreshLayout.OnRefreshListener) {
    this.setOnRefreshListener(listener)

    setColorSchemeResources(
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light
    )
}

@BindingAdapter("isRefresh")
fun SwipeRefreshLayout.bindIsRefresh(isRefreshing: Boolean) {
    this.isRefreshing = isRefreshing
}