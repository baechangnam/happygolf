package com.artilearn.happygolfgo.data.log

import com.artilearn.happygolfgo.data.LogDetail
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LogInfo(
    @SerializedName("lessonLog")
    @Expose
    val lessonLog: LogDetail
)