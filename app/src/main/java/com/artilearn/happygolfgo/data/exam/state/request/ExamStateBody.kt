package com.artilearn.happygolfgo.data.exam.state.request

import com.google.gson.annotations.SerializedName

data class ExamStateBody(
    @SerializedName("yearMonth") val yearMonth: String
)