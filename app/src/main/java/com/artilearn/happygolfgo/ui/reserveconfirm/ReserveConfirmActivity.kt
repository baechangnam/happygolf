package com.artilearn.happygolfgo.ui.reserveconfirm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ActivityReserveConfirmBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.reservesuccess.ReserveSuccessActivity
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeActivity
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.activity_reserve_confirm.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
//comments: keyword 예약 확인 화면:  예약명 타석이용 정책사항
//DONETODO:  안드로이드는 타석에 대한 정보가 없다. 타석 정보 추가
//doneTODO: 브랜치 명을 표시해 준다.
class ReserveConfirmActivity :
    BaseActivity<ActivityReserveConfirmBinding>(R.layout.activity_reserve_confirm) {

    private val viewModel: ReserveConfirmViewModel by viewModel()
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }
    private val plateId by lazy { intent.getIntExtra(PLATE_ID, 0) }
    private val reservationType by lazy { intent.getStringExtra(RESERVATION_TYPE) }
    private val ticketTime by lazy { intent.getParcelableExtra<TicketTime>(TICKET_TIME) }
    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private val timeChange by lazy { intent.getIntExtra(PLATE_TIME_CHANGE, 0) }

    private lateinit var secondTicket: Ticket
    private var secondPlateId: Int? = null
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@ReserveConfirmActivity) {
                showNetworkFail()
            }
            serverError.observe(this@ReserveConfirmActivity) { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            }
            status.observe(this@ReserveConfirmActivity) {
                when (status.value) {
                    ReserveConfirmViewModel.ReservationStatus.CANCEL_SUCCESS -> goReservationCancelSuccess()
                    ReserveConfirmViewModel.ReservationStatus.CANCEL_FAIL -> showToast(
                        ToastType.ERROR,
                        getString(R.string.reserve_confirm_cancel_fail)
                    )
                    else -> showToast(
                        ToastType.ERROR,
                        getString(R.string.reserve_confirm_cancel_fail)
                    ) //when expression must be exhaustive
                }
            }
            plateModel.observe(this@ReserveConfirmActivity) {
                if (ticket == null) {
                    secondPlateId = it.plateReservation.id
                    with(it.plateReservation.ticket) {
                        secondTicket = Ticket(
                            id = id,
                            name = name,
                            type = type,
                            period = period,
                            totalCount = totalCount,
                            usedCount = usedCount,
                            onlyOnceToday = onlyOnceToday,
                            remainingCount = remainingCount,
                            isUnlimited = isUnlimited,
                            startDate = it.plateReservation.startDate,
                            endDate = it.plateReservation.endDate,
                            allowToBook = null,
                           ignoreLimit =  null,
                            multipleReservation =  null,
                            maxMultipleCount = null,
                            branchName = null, //doneTODO:
                            multipleBranchTicketUser = null, //doneTODO
                        )
                    }
                }
                if (it.plateReservation.isCancellable) {
                    showChangeTimeLayout()
                    initBottomView(
                        binding.reserveConfirmBottomView.bottom_layout,
                        getString(R.string.reserve_confirm_cancel_bottom_title),
                        BottimViewType.CANCEL_TYPE
                    )
                }

                //DONETODO: 타석이 배정된 경우에만 타석을 보여준다.
                showChangePlateNumberLayout()
                setReservationTicketPlateNumber(it.plateReservation.plateNumber.toString())

                //comments: layout에 있는 resource_id와 데이타를 설정하여 준다.
                setReservationTicketName(it.plateReservation.name)
                //doneTODO: 다중 브랜치 사용자인 경우 브랜치 이름을 넣어준다.
                if (it.plateReservation.multipleBranchTicketUser != null && it.plateReservation.multipleBranchTicketUser == true) {
                    setReservationBranchName(it.plateReservation.branchName)
                }
                setReservationTicketDate(it.plateReservation.startDate)
                setReservationTicketTime(it.plateReservation.startDate, it.plateReservation.endDate)
                setReservationPolicy(it.policy.body)
            }

            lessonModel.observe(this@ReserveConfirmActivity) {
                if (it.lessonReservation.isCancellable) {
                    initBottomView(
                        binding.reserveConfirmBottomView.bottom_layout,
                        getString(R.string.reserve_confirm_cancel_bottom_title),
                        BottimViewType.CANCEL_TYPE
                    )
                }

                setReservationTicketName(it.lessonReservation.name)
                setReservationTicketDate(it.lessonReservation.startDate)
                setReservationTicketTime(
                    it.lessonReservation.startDate,
                    it.lessonReservation.endDate
                )
                if (it.lessonReservation.multipleBranchTicketUser != null && it.lessonReservation.multipleBranchTicketUser == true) {
                    setReservationBranchName(it.lessonReservation.branchName)
                }
            }
            policy.observe(this@ReserveConfirmActivity) {
                setReservationPolicy(it)
            }
            unKnowError.observe(this@ReserveConfirmActivity) {
                showToast(ToastType.WARNNING, "티켓 정보를 알 수 없습니다")
            }
            reservationPlate.observe(this@ReserveConfirmActivity) {
                goReserveSuccessPlate(it)
            }
            reservationLesson.observe(this@ReserveConfirmActivity) {
                goReserveSuccessLesson(it)
            }
            buttonClickable.observe(this@ReserveConfirmActivity) {
                binding.reserveConfirmBottomView.bottom_layout.isClickable = true
            }
            reservationTimeChange.observe(this@ReserveConfirmActivity) {
                goReserveSuccessPlateTime(it)
            }
            appRefresh.observe(this@ReserveConfirmActivity) {
                goRefresh()
            }
        }
    }

    private fun initView() {
        hideChangeTimeLayout()
        initToolbar(reserve_confirm_toolbar.toolbar, getString(R.string.reserve_confirm_toolbar_title), true)

        when (reservationType) {
            "ReservationPlateFromAlram" -> viewModel.getReservationPlateDetail(plateId)
            "ReservationLessonFromAlram" -> {
                viewModel.getReservationLessonDetail(lessonId)
                binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
            }
            "cancel_plate" -> viewModel.getReservationPlateDetail(plateId)
            "cancel_lesson" -> {
                viewModel.getReservationLessonDetail(lessonId)
                binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
            }
            "reservation" -> {
                initBottomView(binding.reserveConfirmBottomView.bottom_layout, getString(R.string.reserve_confirm_bottom_title), BottimViewType.DEFAULT_TYPE)
                //feature/20211222/migration
                //DONETODO check this out, what tickeTime is null
                setupMultiplePlateTime()
                val typedTicketTime : TicketTime? = ticketTime
                val typedTicket : Ticket? = ticket
                if ( typedTicketTime != null && typedTicket != null ) {
                    if (typedTicketTime.plateAvailabilityId != null) {
                        viewModel.ticketId = typedTicket.id
                        viewModel.plateAvailabilityId = typedTicketTime.plateAvailabilityId
                        viewModel.plateTimeChange = timeChange
                        viewModel.requestPolicy()
                    } else {
                        viewModel.ticketId = typedTicket.id
                        viewModel.lessonAvailabilityId = typedTicketTime.lessonAvailabilityId
                        binding.tvTicketPolicy.text =
                            getString(R.string.reserve_confirm_lesson_policy)
                    }
                    setReservationTicketName(typedTicket.name)
                    setReservationTicketDate(typedTicketTime.startDate)
                    setReservationTicketTime(typedTicketTime.startDate, typedTicketTime.endDate)
                }
            }
        }
    }

    private fun setReservationTicketName(name: String) {
        binding.tvTicketName.text = name
    }

    private fun setReservationBranchName(name: String?) {
        name?.let {
            binding.tvReservationNameBranch.text = "예약명(${name})"
        }
    }

    private fun setReservationTicketPlateNumber(plateNumber: String) {
        binding.tvTicketPlateNumber.text = plateNumber
    }

    private fun setReservationTicketDate(startTime: String) {
        val date = convertTime(startTime).trim().split(":")
        val text = "${date[0]}. ${date[1]}. ${date[2]}"
        binding.tvTicketDate.text = text
    }

    private fun setReservationTicketTime(startTime: String, endTime: String) {
        val start = convertTime(startTime).trim().split(":")
        val end = convertTime(endTime).trim().split(":")

        val text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
        binding.tvTicketTime.text = text
    }

    private fun setStartEndTimeByTextView(startTime: String?, endTime: String?, textView:TextView) {
        if (startTime != null && endTime != null) {
            val start = convertTime(startTime).trim().split(":")
            val end = convertTime(endTime).trim().split(":")
            val text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
            textView.visibility = View.VISIBLE
            textView.text = text
        }
    }

    private fun setReservationPolicy(policy: String) {
        binding.tvTicketPolicy.text = policy
    }

    private fun showChangePlateNumberLayout() {
        with(binding) {
            ticketplatenumberLayout.visibility = View.VISIBLE
//            llTimechange.setOnClickListener { goTicketTime() }
        }
    }

    private fun showChangeTimeLayout() {
        with(binding) {
            llTimechange.visibility = View.VISIBLE
            llTimechange.setOnClickListener { goTicketTime() }
        }
    }

    private fun hideChangeTimeLayout() {
        binding.llTimechange.visibility = View.GONE
    }

    private fun setupMultiplePlateTime() {
        if (ticketTime != null && ticketTime?.isMutiple != null && ticketTime?.isMutiple == true) {
            if (ticketTime?.secondStartDate != null && ticketTime?.secondEndDate != null) {
                setStartEndTimeByTextView( ticketTime?.secondStartDate, ticketTime?.secondEndDate, binding.tvTicketTimeSecond )
            }

            if (ticketTime?.thirdStartDate != null && ticketTime?.thirdEndDate != null) {
                setStartEndTimeByTextView( ticketTime?.thirdStartDate, ticketTime?.thirdEndDate, binding.tvTicketTimeThird )
            }
        }
    }

    private fun goReserveSuccessPlate(plate: ReservationPlate) {
        val plateIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_PLATE, plate)
                putExtra(RESERVATION_TYPE, reservationType)
            }
        startActivity(plateIntent)
    }

    private fun goReserveSuccessLesson(lesson: ReservationLesson) {
        val lessonIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_LESSON, lesson)
                putExtra(RESERVATION_TYPE, reservationType)
            }
        startActivity(lessonIntent)
    }

    private fun goReservationCancelSuccess() {
        val cancelIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationType)
            }
        startActivity(cancelIntent)
        finish()
    }

    private fun goTicketTime() {
        //feature/20220216/multiSelection
        val intent = Intent(this, TicketTimeActivity::class.java)

        if (ticket == null) {
            val convertTime = convertDate(secondTicket.startDate)
            val calendar = convertTime?.let { convertCalendar(it) }
            date = "${calendar?.get(Calendar.YEAR)}-${calendar?.get(Calendar.MONTH)?.plus(1)}-${calendar?.get(
                Calendar.DAY_OF_MONTH)}"
            with(intent) {
                putExtra(TICKET, secondTicket)
                putExtra(SELECTION_DATE, date)
                putExtra(PLATE_TIME_CHANGE, secondPlateId)
            }
        } else {
            with(intent) {
                putExtra(TICKET, ticket)
                //DONETODO: check this out ticket is not null, which is assured above
                putExtra(SELECTION_DATE, ticket?.startDate)
                putExtra(PLATE_TIME_CHANGE, plateId)
            }
        }

        startActivity(intent)
    }


    private fun goReserveSuccessPlateTime(plate: ReservationPlate) {
        val plateIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_PLATE, plate)
                putExtra(RESERVATION_TYPE, reservationType)
                putExtra(PLATE_TIME_CHANGE, 1)
            }
        startActivity(plateIntent)
    }

    override fun bottomAction() {
        super.bottomAction()

        when (reservationType) {
            "ReservationPlateFromAlram", "cancel_plate" -> viewModel.reservationPlateCancel(plateId)
            "ReservationLessonFromAlram", "cancel_lesson" -> viewModel.reservationLessonCancel(lessonId)
            "reservation" -> {
                //feature/20211222/migration
                //feature/2022/0216/multiSelection
                //하단의 예약을 클릭 했을 때
                val  typedTicketTime : TicketTime ? = ticketTime
                if (typedTicketTime?.plateAvailabilityId != null) {
                    if (timeChange == 0) { //예약 변경이 아닐 경우
                         if (ticketTime != null && ticketTime?.isMutiple != null  && ticketTime?.isMutiple == true) {
                             viewModel.reservationThreeOnTimePlate(ticketTime?.firstPlateAvailabilityId ?: 0
                                                                        ,ticketTime?.secondPlateAvailabilityId ?: 0
                                                                        ,ticketTime?.thirdPlateAvailabilityId ?: 0)
                         } else {
                             viewModel.reservationPlate()
                         }
                    } else {
                        viewModel.reservationPlateTimeChange()
                    }
                } else {
                    viewModel.reservationLesson()
                }
            }
        }

        binding.reserveConfirmBottomView.bottom_layout.isClickable = false
    }

}