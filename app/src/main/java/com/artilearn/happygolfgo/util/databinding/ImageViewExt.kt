package com.artilearn.happygolfgo.util.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("onBindProfile")
fun ImageView.bindImage(imagePath: String?) {
    if (imagePath.isNullOrEmpty()) {
        Glide.with(this)
            .load(R.drawable.ic_empty_user_profile)
            .into(this)
    } else {
        Glide.with(this)
            .load(imagePath)
            .apply(RequestOptions.bitmapTransform(
                MultiTransformation(
                    CenterCrop(),
                    CircleCrop()
                )
            ))
            .into(this)
    }
}

@BindingAdapter("onBindSwingVideoThumbNail")
fun ImageView.bindSwingVideoThumNail(imagePath: String?) {
    if (imagePath.isNullOrEmpty()) {
        Glide.with(this)
            .load(R.drawable.ic_flag_no_ticket_card)
            .into(this)
    } else {
        Glide.with(this)
            .load(imagePath)
            .apply(RequestOptions.bitmapTransform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(32)
                )
            ))
            .into(this)
    }
}

@BindingAdapter("onBindScoreCardBackground")
fun ImageView.bindScoreCardBackgroundl(imagePath: String?) {
    Glide.with(this)
        .load(R.drawable.bg_training_record_round_scroe_card)
        .apply(RequestOptions.bitmapTransform(
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(120)
            )
        ))
        .into(this)
}

@BindingAdapter("onImage")
fun ImageView.onBindImage(club: String?) {
    when (club) {
        "드라이버" -> this.setImageResource(R.drawable.ic_golf_game_score_dr)
        "우드" -> this.setImageResource(R.drawable.ic_golf_game_score_wood)
        "아이언" -> this.setImageResource(R.drawable.ic_golf_game_score_iron)
        "숏 아이언 / 웻지" -> this.setImageResource(R.drawable.ic_golf_game_score_short)
        "퍼팅" -> this.setImageResource(R.drawable.ic_golf_game_score_putt)
        else -> this.setImageResource(R.drawable.ic_golf_game_score_dr)
    }
}