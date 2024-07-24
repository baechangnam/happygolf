package com.artilearn.happygolfgo.data.home
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JoinMonthlyExaminationResponse (
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: JoinMonthlyExaminationResponseData
)