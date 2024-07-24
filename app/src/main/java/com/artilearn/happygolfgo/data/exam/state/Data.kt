package com.artilearn.happygolfgo.data.exam.state

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("errorMessage") val errorMessage: String,
    @SerializedName("res") val res: Res
)