package com.artilearn.happygolfgo.util.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@BindingAdapter("onClick")
fun View.bindOnClick(listener: View.OnClickListener) {
    setOnClickListener(listener)
}

@BindingAdapter("emptyPauseTicket")
fun View.emptyPasueTicket(result: Boolean) {
    visibility = when (result) {
        true -> View.VISIBLE
        else -> View.INVISIBLE
    }
}

@BindingAdapter("emptyExpiredTicket")
fun View.emptyExpiredTicket(result: Boolean) {
    visibility = when (result) {
        true -> View.VISIBLE
        else -> View.INVISIBLE
    }
}