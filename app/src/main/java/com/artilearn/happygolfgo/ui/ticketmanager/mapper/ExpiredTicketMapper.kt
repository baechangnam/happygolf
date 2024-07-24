package com.artilearn.happygolfgo.ui.ticketmanager.mapper

import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemoteExpiredTicketModel
import com.artilearn.happygolfgo.mapper.Mapper
import com.artilearn.happygolfgo.ui.ticketmanager.model.ExpiredModel

object ExpiredTicketMapper : Mapper<RemoteExpiredTicketModel, ExpiredModel> {
    override fun mapper(item: RemoteExpiredTicketModel): ExpiredModel {
        return ExpiredModel(
            type = item.type,
            name = item.name,
            date = dateMapper(item.startDate, item.endDate),
            paid = "${item.price} 원"
        )
    }

    private fun dateMapper(startDate: String, endDate: String): String {
        val start = startDate.split(":")
        val end = endDate.split(":")

        return "${start[1]}.${start[2]} ~ ${end[1]}.${end[2]}"
    }

}