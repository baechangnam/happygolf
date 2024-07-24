package com.artilearn.happygolfgo.util

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.exam.Exam
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPower
import com.artilearn.happygolfgo.data.exam.golfpower.Res
import com.artilearn.happygolfgo.data.examination.Examination
import com.artilearn.happygolfgo.ui.home.record.*
import com.artilearn.happygolfgo.ui.home.record.model.RankingBySubjectData
import com.artilearn.happygolfgo.ui.home.record.model.RankingInAllBranchData
import com.artilearn.happygolfgo.ui.home.record.model.RankingInBranchData
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.ui.home.reservation.ReservationAdapterWithHeader
import com.artilearn.happygolfgo.ui.home.reservation.ReservationViewModel
import com.artilearn.happygolfgo.ui.home.reservation.TodaysReservationAdapter
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListAdapter
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeAdapter
import com.artilearn.happygolfgo.util.BaseUtils.context
import java.util.*

//comments: DataBinding: layout.xml에 있는 attribute를 통하여 값을 전달한다.
//DataBinding에 대한 부분이 여기에 들어 있다. Eddie Kim
@BindingAdapter("setTicketType")
fun setTicketType(iv: ImageView, type: Int) {
    when (type) {
        0 -> iv.setImageResource(R.drawable.ic_reservation_lesson_point)
        2 -> iv.setImageResource(R.drawable.ic_home_reservation_ticket_point)
        else -> iv.setImageResource(R.drawable.ic_home_reservation_ticket_point)
    }
}

@BindingAdapter("setTicketStartDate", "setTicketEndDate")
fun ticketDate(tv: TextView, startDate: String, endDate: String) {
    val startTime = convertTime(startDate).trim().split(":")
    val endTime = convertTime(endDate).trim().split(":")

    val text = "${startTime[1]}.${startTime[2]}~${endTime[1]}.${endTime[2]}"
    tv.text = text
}

@BindingAdapter("setTicketStartDateInShortYear", "setTicketEndDateInShortYear")
fun ticketDateInShortYear(tv: TextView, startDate: String, endDate: String) {
    val startTime = convertTimeInShortYear(startDate).trim().split(":")
    val endTime = convertTimeInShortYear(endDate).trim().split(":")
    val text =  "${startTime[0]}.${startTime[1]}.${startTime[2]}\n~${endTime[0]}.${endTime[1]}.${endTime[2]}"
    tv.text = text
}

@BindingAdapter(
    "setTicketTotalCount",
    "setTicketRemainingCount",
    "setTicketIsUnLimited",
    "setTicketOnlyOnceToday"
)
fun ticketCount(
    tv: TextView,
    total: Int,
    remaining: Int,
    isUnlimited: Boolean,
    onceToday: Boolean
) {
    if (isUnlimited && onceToday) {
        tv.text = "1일 1회 사용"
    } else if (isUnlimited && !onceToday) {
        tv.text = "무제한 사용"
    } else {
        val text = "${total}회 중 ${remaining}회 남음"
        tv.text = text
    }
}


@BindingAdapter("setTicketsHeaderView", "setScrollHeaderView")
fun RecyclerView.setTicketItemsHeaderView(ticket: List<Ticket>?, vm: ReservationViewModel) {
        with(adapter as ReservationAdapterWithHeader) {
        ticket?.let {
            this.addItems(it)
        }
    }

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (recyclerView.scrollState) {
                0 -> vm.onButtonState(true)
                else -> vm.onButtonState(false)

            }
        }
    })
}

@BindingAdapter("setTickets", "setScroll")
fun RecyclerView.setTicketItems(ticket: List<Ticket>?, vm: ReservationViewModel) {
    with(adapter as ReservationAdapterWithHeader) {
        ticket?.let {
            this.addItems(it)
            //this.addItems(it)
            //this.addItem(it)
            //this.submitList(it)
        }
    }

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (recyclerView.scrollState) {
                0 -> vm.onButtonState(true)
                else -> vm.onButtonState(false)

            }
        }
    })
}

@BindingAdapter("setExamination")
fun RecyclerView.setExamination(examination: Examination?) {
    with(adapter as ReservationAdapterWithHeader) {
        examination?.let {
            this.setExamination(examination)
        }
    }
}


@BindingAdapter("setLessonLog")
fun RecyclerView.setLessonLogItems(logs: List<LessonLog>?) {
    with(adapter as LessonPostListAdapter) {
        this.clear()
        logs?.let { this.setItems(it) }
    }
}


@BindingAdapter("setSwingVideoItems")
fun RecyclerView.setSwingVideoItems(items: List<TRSummarySwingVideoRecordModel>?) {
    with(adapter as SwingVideoListAdapter) {
        this.clear()
        items?.let { this.setItems(it) }
    }
}

@BindingAdapter("setRankingInBranchDataItems")
fun RecyclerView.setRankingInBranchDataItems(items: List<RankingInBranchData>?) {
    with(adapter as GolfRankingInBranchAdapter) {
        this.clear()
        items?.let { this.setItems(it) }
    }
}

@BindingAdapter("setRankingBySubjectDataItems")
fun RecyclerView.setRankingBySubjectDataItems(items: List<RankingBySubjectData>?) {
    with(adapter as GolfRankingBySubjectAdapter) {
        this.clear()
        items?.let { this.setItems(it) }
    }
}

@BindingAdapter("setRankingAllBranchDataItems")
fun RecyclerView.setRankingAllBranchDataItems(items: List<RankingInAllBranchData>?) {
    with(adapter as GolfRankingInAllBranchAdapter) {
        this.clear()
        items?.let { this.setItems(it) }
    }
}

@BindingAdapter("setLogTime", "setLogCoachName")
fun setLessonLogItem(tv: TextView, time: String, coach: String) {
    val logTime = convertTime(time).trim().split(":")
    val text = "${logTime[0]}. ${logTime[1]}. ${logTime[2]} / $coach"
    tv.text = text
}

@BindingAdapter("setSwingVideoClub", "setSwingVideoDist", "setSwingVideoShortUnit")
fun setSwingVideoClubDistItem(tv: TextView, club: String, dist: String, shortUnit:String) {
    val text = "$club ・ ${dist}${shortUnit}"
    tv.text = text
}

@BindingAdapter("setSwingVideoTime", "setSwingVideoPosition")
fun setSwingVideoTimePosItem(tv: TextView, time: String, pos:String) {
    try {
        val logTime = convertTime(time).trim().split(":")
        val text = "${logTime[0]}. ${logTime[1]}. ${logTime[2]} / ${pos}"
        tv.text = text
    } catch (e: Exception) {
        tv.text = ""
    }
}

@BindingAdapter("setRoundDateTime")
fun setRoundDateTimeItem(tv: TextView, time: String) {
    try {
        val logTime = convertTime(time).trim().split(":")
        val text = "${logTime[0]}. ${logTime[1]}. ${logTime[2]}"
        tv.text = text
    } catch (e: Exception) {
        tv.text = ""
    }
}

@BindingAdapter("setSwingVideoDateTime", "setSwingVideoPosition")
fun setSwingVideoDateTime(tv: TextView, time: String, position:String) {
    try {
        val logTime = convertTime(time).trim().split(":")
        val trimmedMonth = logTime[1].trim()
        val trimmedDay = logTime[2].trim()
        val text = "${logTime[0]}년 ${trimmedMonth}월 ${trimmedDay}일 $position"
        tv.text = text
    } catch (e: Exception) {
        tv.text = ""
    }
}

@BindingAdapter("setSwingVideoRecordClub", "setSwingVideoRecordDist")
fun setSwingVideoRecordClubDistItem(tv: TextView, club: String, dist:String) {
    try {
        val text = "${club} . ${dist}m"
        tv.text = text
    } catch (e: Exception) {
        tv.text = ""
    }
}

@BindingAdapter("setAlramDate")
fun setAlramDate(tv: TextView, time: String) {
    val long = convertStringtoLong(time)
    val realTime = Date(long).convertDateTime()
    tv.text = realTime
}

@BindingAdapter("setAlramTitle")
fun setAlramTitle(tv: TextView, title: String?) {
    title?.let {
        tv.text = it
    }
}

@BindingAdapter("setAlramBody")
fun setAlramBody(tv: TextView, body: String?) {
    body?.let {
        tv.text = it
    }
}

@BindingAdapter("setTrainBody")
fun setTrainBody(tv: TextView, body: String?) {
    body?.let {
        tv.text = it
    }
}

@BindingAdapter("setTicketTime")
fun RecyclerView.setTicketTimeItem(ticketTime: List<TicketTime>?) {
    with(adapter as TicketTimeAdapter) {
        this.clear()
        ticketTime?.let { setItems(it) }
    }
}

@BindingAdapter("setTicketTimeStart", "setTicketTimeEnd")
fun setTicketTimeDate(tv: TextView, start: String, end: String) {
    val startTime = convertTime(start).trim().split(":")
    val endTime = convertTime(end).trim().split(":")

    val text = "${startTime[3]}:${startTime[4]}~${endTime[3]}:${endTime[4]}"
    tv.text = text
}

@BindingAdapter("setTicketLessonStatus", "setTicketPlateAllCount")
fun setTicketTimeTitle(tv: TextView, lessonStauts: Int?, plateAllCount: Int?) {
    lessonStauts?.let {
        tv.text = "상태"
    }
    plateAllCount?.let {
        val text = "총 ${plateAllCount}석"
        tv.text = text
    }
}

@BindingAdapter("setTicketLessonNowStatus", "setTicketPlateAvailableCount")
fun setTicketTimeDetail(tv: TextView, lessonStatus: Int?, plateAvailableCount: Int?) {
    lessonStatus?.let {
        tv.text = "예약가능"
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_black))
    }

    plateAvailableCount?.let {
        if (it != 0) {
            val text = "잔여 ${it}석"
            tv.text = text
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_black))
        } else {
            tv.text = "예약불가"
            tv.setTypeface(null, Typeface.BOLD)
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_red))
        }
    }
}

@BindingAdapter("setTicketName")
fun setTicketName(tv: TextView, name: String) {
    tv.text = name
}

//doneTODO: 멀티 브랜치 사용자 정보
@BindingAdapter("setMultiUserBranchName")
fun setMultiUserBranchName(tv: TextView, name: String?) {
    name?.let {
        tv.text = name
    }
}

//@BindingAdapter("setLogThumbNail")
//fun setLogThumbNail(iv: ImageView, thumbNail: String?) {
//    thumbNail?.let {
//        iv.setImage(iv, it)
//    }
//}

@BindingAdapter("setShotTypeImageIndex")
fun setShotTypeImageIndex(iv: ImageView, imageIndex: String) {
    when(imageIndex) {
         "1" ->  iv.setImageResource(R.drawable.img_shot_type_image_1)
        "2" ->  iv.setImageResource(R.drawable.img_shot_type_image_2)
        "3" ->  iv.setImageResource(R.drawable.img_shot_type_image_3)
        "4" ->  iv.setImageResource(R.drawable.img_shot_type_image_4)
        "5" ->  iv.setImageResource(R.drawable.img_shot_type_image_5)
        "6" ->  iv.setImageResource(R.drawable.img_shot_type_image_6)
        "7" ->  iv.setImageResource(R.drawable.img_shot_type_image_7)
        else ->  iv.setImageResource(R.drawable.img_shot_type_image_1)
    }
}

@BindingAdapter("setGolfWeatherIconInMyGolfPower")
fun setGolfWeatherIconInMyGolfPower(iv: ImageView, iconType: String?) {
    if (iconType != null) {
        iv.visibility = View.VISIBLE
        when (iconType) {
            "1" -> iv.setImageResource(R.drawable.my_golf_power_score_board_weather_sunny)
            "2" -> iv.setImageResource(R.drawable.my_golf_power_score_board_weather_cloudy)
            "3" -> iv.setImageResource(R.drawable.my_golf_power_score_board_weather_partly_sunny)
            "4" -> iv.setImageResource(R.drawable.my_golf_power_score_board_weather_rainy)
            else -> {
                iv.visibility = View.INVISIBLE
            }
        }
    } else {
        iv.visibility = View.INVISIBLE
    }
}

@BindingAdapter("setScoreIndicatorIconInMyGolfPower")
fun setScoreIndicatorIconInMyGolfPower(iv: ImageView, indicator: String?) {
    if (indicator != null) {
        iv.visibility = View.VISIBLE
        when (indicator) {
            "up" -> iv.setImageResource(R.drawable.my_golf_power_exam_result_page_up_arrow)
            "even" -> iv.setImageResource(R.drawable.my_golf_power_exam_result_page_even_arrow)
            "down" -> iv.setImageResource(R.drawable.my_golf_power_exam_result_page_down_arrow)
            else -> {
                iv.visibility = View.INVISIBLE
            }
        }
    } else {
        iv.visibility = View.INVISIBLE
    }
}

//@BindingAdapter("setLogComment")
//fun setLogComment(tv: TextView, comment: String?) {
//    comment?.let {
//        tv.text = it
//    }
//}

@BindingAdapter("setReservationStartTime")
fun setReservationStartTime(tv: TextView, startTime: String) {
    val time = convertTime(startTime).trim().split(":")
    tv.text = String.format("%s:%s", time[3], time[4])
}

@BindingAdapter("setReservationEndTime")
fun setReservationEndTime(tv: TextView, endTime: String) {
    val time = convertTime(endTime).trim().split(":")
    tv.text = String.format("%s:%s", time[3], time[4])
}

@BindingAdapter("setReservationPoint")
fun setReservationType(
    iv: ImageView,
    plateNumber: Int?
) {
    if (plateNumber == null) {
        iv.setImageResource(R.drawable.ic_reservation_lesson_point)
    } else {
        iv.setImageResource(R.drawable.ic_home_reservation_ticket_point)
    }
}

@BindingAdapter("setReservationCalendarColor")
fun setReservationCalendarColor(
    cl: ConstraintLayout,
    plateNumber: Int?
) {
    if (plateNumber == null) {
        cl.setBackgroundResource(R.drawable.bg_reservation_list_calendar_lesson)
    } else {
        cl.setBackgroundResource(R.drawable.bg_reservation_list_calendar_plate)
    }
}

@BindingAdapter("setReservationMonth", "setReservationMonthType")
fun setReservationMonth(
    tv: TextView,
    startDate: String,
    plateNumber: Int?
) {
    val date = convertTime(startDate).trim().split(":")
    val month = date[1].convertMonth()
    tv.text = String.format("%s월", month)

    if (plateNumber == null) {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_lesson_text_month))
    } else {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_plate_text_month))
    }
}

@BindingAdapter("setReservationDay", "setReservationDayType")
fun setReservationDay(
    tv: TextView,
    startDate: String,
    plateNumber: Int?
) {
    val date = convertTime(startDate).trim().split(":")
    tv.text = String.format("%s", date[2])

    if (plateNumber == null) {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_lesson_text_day))
    } else {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_plate_text_day))
    }
}

@BindingAdapter("setReservationCard", "setReservationCardDay")
fun setReservationCard(
    cv: CardView,
    plateNumber: Int?,
    today: String
) {
    val currentDate = getNowDate()
    val reservationDate = convertDate(today)

    if (plateNumber == null) {
        if (currentDate.convertString() == reservationDate?.convertString()) {
            cv.setBackgroundResource(R.drawable.bg_reservation_list_card_lesson)
        } else {
            cv.setBackgroundResource(R.drawable.bg_reservation_card_default)
        }
    } else {
        if (currentDate.convertString() == reservationDate?.convertString()) {
            cv.setBackgroundResource(R.drawable.bg_reservation_list_card_plate)
        } else {
            cv.setBackgroundResource(R.drawable.bg_reservation_card_default)
        }
    }
}

@BindingAdapter("setMedalImageInSubjectInRanking")
fun setMedalImageInSubjectInRanking(v: View, type: String) {
    when (type) {
        "1" ->  v.setBackgroundResource(R.drawable.ic_grade_1)
        "2" ->  v.setBackgroundResource(R.drawable.ic_grade_2)
        "3" ->  v.setBackgroundResource(R.drawable.ic_grade_3)
        "4" ->  v.setBackgroundResource(R.drawable.ic_grade_4)
        "5" ->  v.setBackgroundResource(R.drawable.ic_grade_5)
        else ->v.setBackgroundResource(R.drawable.ic_grade_1)
    }
}

@BindingAdapter("setTicketType")
fun setTicketType(cardView: CardView, type: Int) {
    when (type) {
        0 -> cardView.setCardBackgroundColor(context.getColor(R.color.orange100))
        1 -> cardView.setCardBackgroundColor(context.getColor(R.color.green100))
        else -> cardView.setCardBackgroundColor(context.getColor(R.color.blue100))
    }
}

@BindingAdapter("setTicketType")
fun setTicketType(textView: TextView, type: Int) {
    when (type) {
        0 -> {
            textView.text = "레슨"
            textView.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange500))
        }
        1 -> {
            textView.text = "그룹"
            textView.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green500))
        }
        else -> {
            textView.text = "타석"
            textView.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue500))
        }
    }
}

@BindingAdapter("setTickCountText")
fun setTickCountText(textView: TextView, count: Int) {
    textView.text = count.toString()
}

@BindingAdapter("setIntToText")
fun setIntToText(textView: TextView, value: Int) {
    textView.text = value.toString()
}

@BindingAdapter("setFloatToText")
fun setFloatToText(textView: TextView, value: Float) {
    textView.text = value.toInt().toString()
}

@BindingAdapter("setTotalRank")
fun setTotalRank(textView: TextView, value: String) {
    textView.text = " / $value"
}

@BindingAdapter("setChangeRank")
fun setChangeRank(textView: TextView, golfPower: GolfPower) {
    if(golfPower.indicator == "up"){
        textView.text = "+${golfPower.delta}"
        textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green100))
        textView.setTextColor(context.getColor(R.color.green500))
    } else if(golfPower.indicator == "down") {
        textView.text = "-${golfPower.delta}"
        textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_100))
        textView.setTextColor(context.getColor(R.color.red))
    } else {
        textView.text = "-"
        textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray100))
        textView.setTextColor(context.getColor(R.color.gray500))
    }
}

@BindingAdapter("setTodaysReservation", "setViewModel")
fun RecyclerView.setTodaysReservation(ticket: List<String>?, vm: ReservationViewModel) {
    with(adapter as TodaysReservationAdapter) {
        ticket?.let {
            this.addItems(it)
        }
    }
}

@BindingAdapter("setExamState")
fun TextView.setExamState(examState: Exam.ExamState?) {
    examState ?: return
    when(examState) {
        Exam.ExamState.waiting -> {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    BaseUtils.context, R.color.gray300)
            )
            setTextColor(BaseUtils.context.getColor(R.color.gray400))
            text = "대기중"
            isEnabled = false
        }
        Exam.ExamState.taking -> {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    BaseUtils.context, R.color.green500)
            )
            setTextColor(BaseUtils.context.getColor(R.color.color_white))
            text = "응시중"
            isEnabled = false
        }
        Exam.ExamState.done -> {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    BaseUtils.context, R.color.gray300)
            )
            setTextColor(BaseUtils.context.getColor(R.color.gray400))
            text = "응시완료"
            isEnabled = false
        }
        Exam.ExamState.not -> {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    BaseUtils.context, R.color.green500)
            )
            setTextColor(BaseUtils.context.getColor(R.color.color_white))
            text = "도움말"
            isEnabled = true
        }
        Exam.ExamState.open -> {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    BaseUtils.context, R.color.blue500)
            )
            setTextColor(BaseUtils.context.getColor(R.color.color_white))
            text = "응시하기"
            isEnabled = true
        }
    }
}

@BindingAdapter("setAvgGolfPower")
fun TextView.setAvgGolfPower(golfPower: Res?) {
    golfPower ?: return
    text = "${golfPower.avg.title} ${golfPower.avg.score}점"
}

@BindingAdapter("setImprovedScore")
fun TextView.setImprovedScore(score: String?) {
    score ?: return
    text = "${score}점"
}

@BindingAdapter("setAvgGolfPowerPercent")
fun TextView.setAvgGolfPowerPercent(golfPower: Res?) {
    golfPower ?: return
    if(golfPower.avg.indicator == "up") {
        text = " (+${golfPower.avg.delta})"
        setTextColor(BaseUtils.context.getColor(R.color.green500))
    } else if(golfPower.avg.indicator == "down"){
        text = " (${golfPower.avg.delta})"
        setTextColor(BaseUtils.context.getColor(R.color.red))
    } else {
        text = " (${golfPower.avg.delta})"
        setTextColor(BaseUtils.context.getColor(R.color.gray500))
    }
}

@BindingAdapter("setRanking")
fun TextView.setRanking(ranking: String?) {
    ranking ?: return
    text = "${ranking}등"
}

@BindingAdapter("setTotalParticipate")
fun TextView.setTotalParticipate(particitateCount: String?) {
    particitateCount ?: return
    text = "총 ${particitateCount}명이 참여중입니다."
}

@BindingAdapter("setUpdateDate")
fun TextView.setUpdateDate(updateDate: String?) {
    updateDate ?: return
    text = "Updated $updateDate"
}


@BindingAdapter("setTextEmptyGone")
fun TextView.setTextEmptyGone(text: String) {
    isVisible = text.isNotEmpty()
}
