package com.artilearn.happygolfgo.data.reserveconfirm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReservationLessonDetail(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: LessonResponse
)

data class LessonResponse(
    @SerializedName("LessonReservation")
    @Expose
    val lessonReservation: LessonReservation
)

data class LessonReservation(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("status")
    @Expose
    val status: Int,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean,

    @SerializedName("isCancellable")
    @Expose
    val isCancellable: Boolean,

    @SerializedName("multipleBranchTicketUser")
    @Expose
    val multipleBranchTicketUser: Boolean?,

    @SerializedName("branchName")
    @Expose
    val branchName: String?
)