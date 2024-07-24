package com.artilearn.happygolfgo.data.ticketmanager.source.mapper

import com.artilearn.happygolfgo.data.ticketmanager.TicketModelItem
import com.artilearn.happygolfgo.data.ticketmanager.source.model.PeriodsList
import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemotePasueTicketModel
import com.artilearn.happygolfgo.mapper.Mapper
import com.artilearn.happygolfgo.util.convertTime

object RemotePauseMapper : Mapper<TicketModelItem, RemotePasueTicketModel> {
    override fun mapper(item: TicketModelItem): RemotePasueTicketModel {
        return RemotePasueTicketModel(
            name = item.name,
            type = item.type,
            startDate = convertTime(item.startDate),
            endDate = convertTime(item.endDate),
            pauseList = item.pasuePeriods.map { remotePeriods ->
                PeriodsList(
                    startDate = convertTime(remotePeriods.startDate),
                    endDate = convertTime(remotePeriods.endDate)
                )
            }
        )
    }

}