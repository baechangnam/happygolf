package com.artilearn.happygolfgo.data.splash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SynchronizedResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("resultCode")
    @Expose
    val resultCode: Int? = null,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: SynchronizedDetail
)