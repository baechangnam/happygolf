package com.artilearn.happygolfgo.data.exam.state

import com.google.gson.annotations.SerializedName

data class ExamStateDto(
    @SerializedName("Data") val data: Data,
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int
)