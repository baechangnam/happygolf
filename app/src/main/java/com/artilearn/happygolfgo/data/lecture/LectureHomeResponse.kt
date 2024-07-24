package com.artilearn.happygolfgo.data.lecture

import com.artilearn.happygolfgo.data.LectureHome
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LectureHomeResponse(

    @SerializedName("sections")
    @Expose
    val lectureHome: List<LectureHome>

)