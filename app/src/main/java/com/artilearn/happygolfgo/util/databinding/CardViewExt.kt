package com.artilearn.happygolfgo.util.databinding

import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R

@BindingAdapter("onMyRank")
fun CardView.bindCard(isMe: Boolean) {
    if (isMe) {
        this.setBackgroundResource(R.drawable.bg_reservation_list_card_plate)
    } else {
        this.setBackgroundResource(R.drawable.bg_reservation_card_default)
    }
}