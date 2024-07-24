package com.artilearn.happygolfgo.data.exam

import com.google.gson.annotations.SerializedName

data class ExamDto(
    @SerializedName("Data") val data: Data,
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int
)