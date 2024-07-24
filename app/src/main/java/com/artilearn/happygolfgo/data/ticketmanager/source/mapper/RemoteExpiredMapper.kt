package com.artilearn.happygolfgo.data.ticketmanager.source.mapper

import com.artilearn.happygolfgo.data.ticketmanager.TicketModelItem
import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemoteExpiredTicketModel
import com.artilearn.happygolfgo.mapper.Mapper
import com.artilearn.happygolfgo.util.convertTime

object RemoteExpiredMapper : Mapper<TicketModelItem, RemoteExpiredTicketModel> {
    override fun mapper(item: TicketModelItem): RemoteExpiredTicketModel {
        return RemoteExpiredTicketModel(
            type = item.type,
            name = item.name,
            startDate = convertTime(item.startDate),
            endDate = convertTime(item.endDate),
            price = item.totalPrice
        )
    }
}