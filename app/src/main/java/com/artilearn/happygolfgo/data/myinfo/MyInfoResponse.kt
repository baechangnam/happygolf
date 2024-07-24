package com.artilearn.happygolfgo.data.myinfo

import com.artilearn.happygolfgo.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyInfoResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: User
)