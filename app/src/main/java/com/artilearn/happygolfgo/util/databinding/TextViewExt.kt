package com.artilearn.happygolfgo.util.databinding

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.ticketmanager.TicketPausePeriodsModel
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.EXPIRED_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.PAUSE_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.model.PausesPeriodsList
import com.artilearn.happygolfgo.util.convertTime

@BindingAdapter("onRank", "onIsMe")
fun TextView.bindRank(rank: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = rank
}

@BindingAdapter("onNickName", "onIsMe")
fun TextView.bindNickName(nickName: String?, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }

    this.text = nickName ?: "닉네임 없음"
}

@BindingAdapter("onAverages", "onIsMe")
fun TextView.bindAverages(averages: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = averages
}

@BindingAdapter("pauseList")
fun TextView.pauseTicketText(item: List<String>?) {
    item?.let {
        val sb = StringBuilder()

        for( i in item.indices) {
            if (item.lastIndex == i) {
                sb.append(item[i])
            } else {
                sb.append("${item[i]}\n")
            }
        }

        text = sb
    }
}

@BindingAdapter("pauseText")
fun TextView.pauseText(item: Ticket) {
    when (item.viewType) {
        PAUSE_TICKET -> {
            text = "정지"
            setTextColor(ContextCompat.getColor(context, R.color.color_green))
        }
        EXPIRED_TICKET -> {
            text = "만료"
            setTextColor(ContextCompat.getColor(context, R.color.color_red))
        }
    }
}

@BindingAdapter("pauseTicketPeriodsList")
fun TextView.setPausePeriodsText(item: List<TicketPausePeriodsModel>?) {
    item?.let {
        val sb = StringBuilder()

        val pausePeriodsList = remotePauseMapper(it)

        for(i in pausePeriodsList.indices) {
            if (pausePeriodsList.lastIndex == i) {
                sb.append(pausePeriodsList[i].date)
            } else {
                sb.append("${pausePeriodsList[i].date}\n")
            }
        }

        text = sb
    }
}

private fun remotePauseMapper(item: List<TicketPausePeriodsModel>): List<PausesPeriodsList> {
    return item.map {
        PausesPeriodsList(
            date = dateMapper(convertTime(it.startDate), convertTime(it.endDate))
        )
    }
}

private fun dateMapper(startDate: String, endDate: String): String {
    val start = startDate.split(":")
    val end = endDate.split(":")

    return "${start[1]}.${start[2]} ~ ${end[1]}.${end[2]}"
}