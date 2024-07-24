package com.artilearn.happygolfgo.data.splash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SplashTokenResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: SplashTokenDetail
)