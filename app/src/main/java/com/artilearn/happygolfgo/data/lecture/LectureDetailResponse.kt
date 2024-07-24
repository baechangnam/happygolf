package com.artilearn.happygolfgo.data.lecture

import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.data.LectureComment


data class LectureDetailResponse(
    val lect_no: String,
    val lect_title: String,
    val rsc_url: String,
    val rsc_size: String,
    val thmn_url: String,
    val sect_no: String,
    var view_cnt: String,
    val like_cnt: String,
    val sect_label: String,
    val reg_date: String,
    val like_yn: String,
    val favr_yn: String,
    val comments : List<LectureComment>,
    val recommendLectures : List<Lecture>,
    val sectLectures : List<Lecture>


)