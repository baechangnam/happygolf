package com.artilearn.happygolfgo.data.lecture

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LectureBookMarkResponse(
//    @SerializedName("statusCode")
//    @Expose
//    val statusCode: Int,
//
//    @SerializedName("message")
//    @Expose
//    val message: String,

    @SerializedName("serverStatus")
    @Expose
    val serverStatus: String

)