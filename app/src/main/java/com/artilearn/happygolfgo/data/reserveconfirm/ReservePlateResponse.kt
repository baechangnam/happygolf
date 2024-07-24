package com.artilearn.happygolfgo.data.reserveconfirm

import com.artilearn.happygolfgo.data.ReservationPlate
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReservePlateResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: ReservePlateInfo,

    @SerializedName("resultCode")
    @Expose
    val resultCode: Int
)

data class ReservePlateInfo(
    @SerializedName("plateReservation")
    @Expose
    val plateReservation: ReservationPlate
)