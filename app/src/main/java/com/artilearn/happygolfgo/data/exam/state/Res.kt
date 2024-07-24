package com.artilearn.happygolfgo.data.exam.state

import com.artilearn.happygolfgo.data.exam.Exam
import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("state") val state: Exam.ExamState
)