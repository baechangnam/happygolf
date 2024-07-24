package com.artilearn.happygolfgo.data.ticketmanager.source.model


data class RemotePasueTicketModel(
    val name: String,
    val type: Int,
    val startDate: String,
    val endDate: String,
    val pauseList: List<PeriodsList>
)

data class PeriodsList(
    val startDate: String,
    val endDate: String
)

data class RemoteExpiredTicketModel(
    val type: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val price: Int
)