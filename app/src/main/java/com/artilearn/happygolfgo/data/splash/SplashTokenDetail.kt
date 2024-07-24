package com.artilearn.happygolfgo.data.splash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SplashTokenDetail(
    @SerializedName("accessToken")
    @Expose
    val accessToken: String
)