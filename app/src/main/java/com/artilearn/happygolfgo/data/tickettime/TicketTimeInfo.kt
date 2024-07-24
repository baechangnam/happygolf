package com.artilearn.happygolfgo.data.tickettime

import com.artilearn.happygolfgo.data.TicketTime
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TicketTimeInfo(
    @SerializedName("availableTimes")
    @Expose
    val availableTimes: List<TicketTime>
)