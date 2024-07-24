package com.artilearn.happygolfgo.data.reserveconfirm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlateTimeChangeResponse(
    @SerializedName("resultCode")
    @Expose
    val resultCode: Int,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: ChangePlateReservation
) {
    data class ChangePlateReservation(
        @SerializedName("plateReservation")
        @Expose
        val plateReservation: TimePlateReservation
    )

    data class TimePlateReservation(
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
        val plateNumber: String,

        @SerializedName("remainingCount")
        @Expose
        val remainingCount: Int,

        @SerializedName("totalCount")
        @Expose
        val totalCount: Int,

        @SerializedName("isUnlimited")
        @Expose
        val isUnlimited: Boolean,

        @SerializedName("isCancellable")
        @Expose
        val isCancellable: Boolean
    )

}