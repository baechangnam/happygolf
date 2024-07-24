package com.artilearn.happygolfgo.data.exam.golfpower

import com.google.gson.annotations.SerializedName

data class GolfPowerExamResultDto(
    @SerializedName("Data") val data: Data,
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int
)