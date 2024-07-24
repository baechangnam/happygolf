package com.artilearn.happygolfgo.data.lessonlog

import com.artilearn.happygolfgo.data.LessonLog
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LessonLogInfo(
    @SerializedName("lessonLogs")
    @Expose
    val lessonLogs: List<LessonLog>
)