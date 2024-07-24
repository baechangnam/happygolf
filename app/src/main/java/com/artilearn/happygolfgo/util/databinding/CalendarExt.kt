package com.artilearn.happygolfgo.util.databinding

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateViewModel
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.snackbar
import java.util.*

@BindingAdapter("bindAvailablie", "bindEvent", "bindTicket", "bindClick")
fun CalendarView.bindDays(
    available: List<String>?,
    events: List<String>?,
    ticket: Ticket,
    viewModel: TicketDateViewModel
) {
    val calendarView = this
    val availableList: MutableList<Calendar> = mutableListOf()
    val eventDays: MutableList<Calendar> = mutableListOf()

//    setForwordImage(this, ContextCompat.getDrawable(this.context, R.drawable.ic_calendar_forward))
//    setPreviosImage(this, ContextCompat.getDrawable(this.context, R.drawable.ic_calendar_previous))
    setMinimumDate(minimumDate())
    setMaximumDate(maximumDate())

    available?.let {
        availableList.addAll(setServerDates(it))
        setDisabledDays(availableList)
        visibility = View.VISIBLE

        setCalendarDayLayout(R.layout.custom_calendar_day)
    }

    events?.let {
        for (i in events.indices) {
            val date = convertDate(events[i])
            val calendar = if (date != null) {
                convertCalendar(date)
            } else {
                null
            }
            calendar?.let { eventDays.add(it) }
        }
        setEvents(setDots(eventDays))
    }

    setOnDayClickListener(object : OnDayClickListener {
        override fun onDayClick(eventDay: EventDay) {
            val day = "${eventDay.calendar[Calendar.YEAR]}년 " +
                    "${eventDay.calendar[Calendar.MONTH].plus(1)}월 " +
                    "${eventDay.calendar[Calendar.DAY_OF_MONTH]}일"
            val date =
                "${eventDay.calendar.get(Calendar.YEAR)}-${eventDay.calendar.get(Calendar.MONTH) + 1}" +
                        "-${eventDay.calendar.get(Calendar.DAY_OF_MONTH)}"
            if (eventDay.isEnabled) {
                calendarView.snackbar("$day 예약 불가")
            } else {
                when (eventDay.calendar) {
                    in eventDays -> {
                        //조회 2022.02.15 조회 통과 시켜준다.
                        if (ticket.onlyOnceToday) {
                            if (ticket.ignoreLimit != null && ticket.ignoreLimit == true) {
                                viewModel.calendarListenerForViewOnly(ticket, date)
                            } else {
                                calendarView.snackbar("1일 1회 예약 가능합니다")
                            }
                        }
                        else { viewModel.calendarListener(ticket, date)}
                    }
                    else -> viewModel.calendarListener(ticket, date)
                }
            }
        }
    })
}

private fun minimumDate(): Calendar {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

private fun maximumDate(): Calendar {
    return Calendar.getInstance().apply {
        add(Calendar.MONTH, 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

private fun setServerDates(list: List<String>): List<Calendar> {
    val calendarDays = mutableListOf<CalendarDay>()
    val calendars = mutableListOf<Calendar>()
    val min = minimumDate()
    val max = maximumDate()

    val longTime = (max.timeInMillis - min.timeInMillis) / 1000
    val diff = longTime / (24 * 60 * 60)

    for (i in 0 until diff.toInt()) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, i)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        calendarDays.add(CalendarDay(i, calendar))
    }

    for (i in list.indices) {
        val str = convertTime(list[i])
        val strSub = str.split(":")
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, strSub[0].toInt())
            set(Calendar.MONTH, strSub[1].toInt().minus(1))
            set(Calendar.DAY_OF_MONTH, strSub[2].toInt())
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        calendars.add(calendar)
    }

    return calendarDays.filterNot { c ->
        c.calendar in calendars
    }
        .map { it.calendar }
}

private fun setDots(event: List<Calendar>?): List<EventDay> {
    val eventDays = mutableListOf<EventDay>()

    event?.let {
        for (i in it.indices) {
            eventDays.add(EventDay(it[i], R.drawable.ic_calendar_event))
        }
    }

    return eventDays
}

private fun setForwordImage(view: CalendarView,image: Drawable?) =
    image?.let {
        view.apply {
            setForwardButtonImage(it)
        }
    }

private fun setPreviosImage(view: CalendarView, image: Drawable?) =
    image?.let {
        view.apply {
            setPreviousButtonImage(it)
        }
    }

data class CalendarDay(val id: Int, val calendar: Calendar)