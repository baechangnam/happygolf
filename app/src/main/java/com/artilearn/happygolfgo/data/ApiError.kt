package com.artilearn.happygolfgo.data

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("resultCode")
    val resultCode: Int
)