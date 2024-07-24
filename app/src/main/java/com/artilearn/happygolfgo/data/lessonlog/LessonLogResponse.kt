package com.artilearn.happygolfgo.data.lessonlog

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LessonLogResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: LessonLogInfo
)