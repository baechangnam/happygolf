package com.artilearn.happygolfgo.util.databinding

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

@BindingAdapter("checkAlramMessage")
fun ConstraintLayout.checkAlram(checkAlramMessage: MutableLiveData<Boolean>) {
    visibility = when (checkAlramMessage.value) {
        true -> View.VISIBLE
        else -> View.INVISIBLE
    }
}