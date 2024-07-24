package com.artilearn.happygolfgo.data.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JoinMonthlyExaminationResponseRes (
    @SerializedName("state")
    @Expose
    val state: String = "take",

    @SerializedName("golfPowerMonthlyExamID")
    @Expose
    val golfPowerMonthlyExamID: String = "-1",
)

data class JoinMonthlyExaminationResponseData (
    @SerializedName("errorCode")
    @Expose
    val errorCode: Int,

    @SerializedName("errorMessage")
    @Expose
    val errorMessage: String,

    @SerializedName("res")
    @Expose
    val res:JoinMonthlyExaminationResponseRes,
)
