package com.artilearn.happygolfgo.ui.ticketmanager.mapper

import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemotePasueTicketModel
import com.artilearn.happygolfgo.mapper.Mapper
import com.artilearn.happygolfgo.ui.ticketmanager.model.PauseModel

object PauseTicketMapper : Mapper<RemotePasueTicketModel, PauseModel> {
    override fun mapper(item: RemotePasueTicketModel): PauseModel {
        //doneTODO:
        return PauseModel(
            name = item.name,
            type = item.type,
            date = dateMapper(item.startDate, item.endDate),
            pauseList = item.pauseList.map {
                dateMapper(it.startDate, it.endDate)
            }
        )
    }

    private fun dateMapper(startDate: String, endDate: String): String {
        val start = startDate.split(":")
        val end = endDate.split(":")

        return "${start[1]}.${start[2]} ~ ${end[1]}.${end[2]}"
    }
}