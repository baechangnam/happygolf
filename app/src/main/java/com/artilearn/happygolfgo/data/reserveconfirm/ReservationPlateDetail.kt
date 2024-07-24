package com.artilearn.happygolfgo.data.reserveconfirm

import com.artilearn.happygolfgo.data.Ticket
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReservationPlateDetail(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: PlateResponse
)

data class PlateResponse(
    @SerializedName("PlateReservation")
    @Expose
    val plateReservation: PlateReservation,

    @SerializedName("policy")
    @Expose
    val policy: PlatePolicy
)

data class PlateReservation(
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

    @SerializedName("plateNumber")
    @Expose
    val plateNumber: Int,

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
    val branchName: String?,

    @SerializedName("ticket")
    @Expose
    val ticket: Ticket
)

data class PlatePolicy(
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("body")
    @Expose
    val body: String
)

