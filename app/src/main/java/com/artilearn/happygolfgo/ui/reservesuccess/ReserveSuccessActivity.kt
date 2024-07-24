package com.artilearn.happygolfgo.ui.reservesuccess

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.PLATE_TIME_CHANGE
import com.artilearn.happygolfgo.constants.RESERVATION_LESSON
import com.artilearn.happygolfgo.constants.RESERVATION_PLATE
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.databinding.ActivityReserveSuccessBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.reservelist.ReserveListActivity
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.custom_bottom_view.view.*

class ReserveSuccessActivity :
    BaseActivity<ActivityReserveSuccessBinding>(R.layout.activity_reserve_success) {

    private val reservationPlate by lazy { intent.getParcelableExtra<ReservationPlate>(RESERVATION_PLATE) }
    private val reservationLesson by lazy { intent.getParcelableExtra<ReservationLesson>(RESERVATION_LESSON) }
    private val successType by lazy { intent.getStringExtra(RESERVATION_TYPE) }
    private val timeChnage by lazy { intent.getIntExtra(PLATE_TIME_CHANGE, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        initBottomView(binding.reserveSuccessBottomView.bottom_layout, getString(R.string.reserve_success_bottom_title), BottimViewType.DEFAULT_TYPE)
        when (successType) {
            "reservation" -> setReservationText()
            else -> setCancelText()
        }
    }

    private fun setReservationText() {
        with(binding) {
            reservationPlate?.let { plate ->
                if (timeChnage == 0) tvMain.text = getString(R.string.reserve_success_title)
                else tvMain.text = getString(R.string.reserve_success_title_change)
                tvTitle.text = convertToTitle(plate.name, plate.totalCount, plate.remainingCount)
                tvSubtitle.text = convertToTime(plate.startDate, plate.endDate)
                try {
                    if (plate.secondStartDate != null && plate.secondEndDate != null) {
                        if (plate.secondEndDate!!.length > 4 && plate.secondStartDate!!.length > 4) {
                            tvSubtitleSecond.visibility = View.VISIBLE
                            tvSubtitleSecond.text =
                                convertToTime(plate.secondStartDate!!, plate.secondEndDate!!)
                        }
                    } else {

                    }

                    if (plate.thirdStartDate != null && plate.thirdEndDate != null) {
                        if (plate.thirdEndDate!!.length > 4 && plate.thirdStartDate!!.length > 4) {
                            tvSubtitleThird.visibility = View.VISIBLE
                            tvSubtitleThird.text = convertToTime(plate.thirdStartDate!!, plate.thirdEndDate!!)
                        } else {

                        }
                    } else {

                    }
                }  catch (e: Exception) {
                    Log.d("error", e.toString())
                }
            }
            reservationLesson?.let { lesson ->
                tvMain.text = getString(R.string.reserve_success_title)
                tvTitle.text = convertToTitle(lesson.name, lesson.totalCount, lesson.remainingCount)
                tvSubtitle.text = convertToTime(lesson.startDate, lesson.endDate)
            }
        }
    }

    private fun setCancelText() {
        with(binding) {
            tvMain.text = getString(R.string.reserve_cancel_success_title)
        }
    }

    private fun convertToTitle(name: String, allCount: Int, count: Int): String {
        return if (allCount != 0) {
            "$name ( ${allCount}회 중 ${count}남음 )"
        } else {
            "$name ( 무제한 사용권 )"
        }
    }

    private fun convertToTime(start: String, end: String): String {
        val startTime = convertTime(start).trim().split(":")
        val endTime = convertTime(end).trim().split(":")

        return "${startTime[0]}년 ${startTime[1]}월 ${startTime[2]}일 " +
                "${startTime[3]}:${startTime[4]} ~ ${endTime[3]}:${endTime[4]}"
    }

    private fun goHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        startActivity(homeIntent)
    }

    private fun goReservationList() {
        val reservationIntent = Intent(this, ReserveListActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        startActivity(reservationIntent)
    }

    override fun bottomAction() {
        super.bottomAction()

        when (successType) {
            "reservation", "ReservationLessonFromAlram",
            "ReservationPlateFromAlram" -> goHome()
            else -> goReservationList()
        }
    }

}