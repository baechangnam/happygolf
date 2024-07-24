package com.artilearn.happygolfgo.data.ticketmanager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TicketManagerResponse(
    @SerializedName("Data")
    @Expose
    val data: TicketManagerDataKey,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int
)