package com.artilearn.happygolfgo.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lecture(
    val lect_no: String,
    val lect_title: String,
    val rsc_url: String,
    val rsc_size: String,
    val thmn_url: String,
    val sect_no: String,
    var view_cnt: String,
    val like_cnt: String,
    val sect_label: String,
    var like_yn: String,
    var favr_yn: String,
    var watch_yn: String,
    val play_time: String,
    val play_stat_code: String,
    val reg_date: String
) : Parcelable
