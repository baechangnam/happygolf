package com.artilearn.happygolfgo.data.ticketdate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TicketDateInfo(
    @SerializedName("availableDates")
    @Expose
    val availableDates: List<String>,

    @SerializedName("eventDates")
    @Expose
    val eventDates: List<String>
)