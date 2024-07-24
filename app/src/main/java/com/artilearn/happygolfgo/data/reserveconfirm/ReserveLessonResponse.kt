package com.artilearn.happygolfgo.data.reserveconfirm

import com.artilearn.happygolfgo.data.ReservationLesson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReserveLessonResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: ReserveLessonInfo
)

data class ReserveLessonInfo(
    @SerializedName("lessonReservaion")
    @Expose
    val lessonReservaion: ReservationLesson
)