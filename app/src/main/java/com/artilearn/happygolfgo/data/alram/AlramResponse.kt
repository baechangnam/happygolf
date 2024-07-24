package com.artilearn.happygolfgo.data.alram

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AlramResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: AlramInfo
)