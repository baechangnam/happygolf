package com.artilearn.happygolfgo.data.banner

import com.google.gson.annotations.SerializedName

data class BannerDto(
    @SerializedName("Data") val data: Data,
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int
)