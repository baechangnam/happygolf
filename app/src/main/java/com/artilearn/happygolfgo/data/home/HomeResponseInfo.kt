package com.artilearn.happygolfgo.data.home

import com.artilearn.happygolfgo.data.Ticket
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeResponseInfo(
    @SerializedName("Tickets")
    @Expose
    val tickets: List<Ticket>,

    @SerializedName("PausedTickets")
    @Expose
    val pausedTickets: List<Ticket>,

    @SerializedName("ExpiredTickets")
    @Expose
    val expiredTickets: List<Ticket>
)