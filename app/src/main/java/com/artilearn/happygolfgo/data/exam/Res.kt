package com.artilearn.happygolfgo.data.exam

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("exam") val exam: Exam,
    @SerializedName("yearMonths") val yearMonths: List<YearMonth>
)