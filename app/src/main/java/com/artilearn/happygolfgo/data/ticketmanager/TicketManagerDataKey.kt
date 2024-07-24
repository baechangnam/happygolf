package com.artilearn.happygolfgo.data.ticketmanager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TicketManagerDataKey(
    @SerializedName("Tickets")
    @Expose
    val tickets: List<TicketModelItem>
)