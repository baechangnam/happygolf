package com.artilearn.happygolfgo.ui.tickettime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ActivityTicketTimeBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.artilearn.happygolfgo.util.snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar_with_buttons.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketTimeActivity : BaseActivity<ActivityTicketTimeBinding>(R.layout.activity_ticket_time) {

    private val viewModel: TicketTimeViewModel by viewModel()
    private lateinit var adapter: TicketTimeAdapter
    private val reservation = "reservation"

    //comments: 인텐트로 가져온 티켓 데이타
    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private val date by lazy { intent.getStringExtra(SELECTION_DATE) }
    private val timeChange by lazy { intent.getIntExtra(PLATE_TIME_CHANGE, 0) }
    //feature/2022/0216/multiSelection
    // 1일 1회 예약 이지만, 예약 상황을 볼수 있게 한다.
    private val isReservedDay  by lazy { intent.getBooleanExtra(IS_RESERVED_DAY, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@TicketTimeActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@TicketTimeActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@TicketTimeActivity, Observer {
                goRefresh()
            })
//            buttonClickable.observe(this@TicketTimeActivity, Observer {
//                binding.reserveConfirmBottomView.bottom_layout.isClickable = true
//            })
        }
    }

    private fun initView() {
        initToolbar(binding.tickettimeToolbar.toolbar, getString(R.string.tickettime_toolbar_title), true)
        //feature/20211222/migration
        when {
            this.isMultiSelectable() -> {
                //featue/2022/2016/multiSelection
                //하단의 예약하기 버튼으로 대체함.
                //binding.tickettimeToolbar.btnReservation.visibility = View.VISIBLE
                initBottomView(binding.reserveConfirmBottomView.bottom_layout, getString(R.string.tikect_time_activity_bottom_title), BottimViewType.DEFAULT_TYPE)
            }
//            hotfix/2022/0302 하단 액션바 가림 현상
            else -> {
                binding.reserveConfirmBottomView.visibility = View.GONE;
            }
        }
        //DONETODO: be sure  that it's okay if ticket is null
        val typedTicket : Ticket? = ticket
        val typedDate : String? = date
        if (typedTicket != null && typedDate != null ) {
            viewModel.selectTime(typedTicket.type, typedDate, typedTicket.id)
        }
    }

    private fun isReviewOnly() : Boolean {
         return isReservedDay && ticket != null && ticket?.onlyOnceToday == true
    }

    private  fun isMultiSelectable() : Boolean {
//        return false
       return (ticket != null && ticket?.multipleReservation != null && ticket?.maxMultipleCount != null
               && ticket?.multipleReservation == true
//               && ticket?.onlyOnceToday == false
               && timeChange == 0
               && ticket?.allowToBook == true) && !isReviewOnly() && (ticket?.maxMultipleCount!! > 1)
    }

    //feature/2022/0216/multiSelection
    private fun initAdapter() {
        //DONETODO: 예약 불가한 상황이면:  allowBook은 nullable
        val isMultiSectable =  isMultiSelectable()
//        isMultiSectable = true;
        adapter = TicketTimeAdapter(isMultiSectable) { ticketTime, position ->
            when {
                ticketTime.availableCount != null && ticketTime.availableCount == 0 && !isMultiSectable -> {
                    binding.root.snackbar(getString(R.string.tickettime_dont_reservation))
                }
                ticket != null && ticket?.allowToBook != null && ticket?.allowToBook == false  && !isMultiSectable -> {
                    binding.root.snackbar(getString(R.string.ticket_time_activity_error_max_reservation_count))
                }
                isReviewOnly() -> {
                    //ticket_time_activity_error_only_once_day
                    //1일 1회 예약 제한 조건은 넘었지만 예약 볼수 있게 해준다.
                    binding.root.snackbar(getString(R.string.ticket_time_activity_error_only_once_day))
                }
                else -> {
                    if (!isMultiSectable ) {
                        goReserveConfirm(ticketTime)
                    } else {
                        onClickMultipleHandler(ticketTime, position)
                    }
                }
            }
        }
        //feature/2022/0216/multiSelection
        // 예약 다중 선택일 경우 해당하는 이벤트 핸들러를 설정해 준다.
       if (isMultiSectable) {
           setupMultipleSelection()
       }

        binding.recyclerview.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        binding.recyclerview.adapter = adapter
    }

    private fun setupMultipleSelection() : Unit {
        //체크박스 클릭했을 경우
        adapter.onClickCheckBox = { ticketTime, position ->
            onClickMultipleHandler(ticketTime, position)
        }

        binding.root.btnReservation.setOnClickListener {
            //featuer/2022/0216/multiSelection
//            binding.root.snackbar("proceed to reservation")
            onClickReservationHandler()
        }
    }

    private fun onClickReservationHandler() {
        val selectedCount = adapter.getSelectedItemCount()
        if ( selectedCount == 0 ) {
            binding.root.snackbar("예약 시간을 선택하여 주십시요 ")
        } else {
            goMultipleRservationConfirm()
        }
    }

    private fun  onClickMultipleHandler( ticketTime: TicketTime, position:Int) {
        if (ticket != null && ticket?.maxMultipleCount != null) {
            var selectedCount = adapter.getSelectedItemCount()
            if ( ticketTime.isChecked.not() ) {
                if (selectedCount < ticket?.maxMultipleCount!!) {
                    ticketTime.isChecked = ticketTime.isChecked.not()
                } else {
                    binding.root.snackbar("최대 횟수 초과입니다.")
                }
            } else {
                ticketTime.isChecked = ticketTime.isChecked.not()
            }
            selectedCount = adapter.getSelectedItemCount()
            updateTitleText("시간 선택 (${selectedCount}/${ticket?.maxMultipleCount})")
            adapter.notifyItemChanged(position)
        }
    }

    private fun updateTitleText( title:String) {
        val actionBar = supportActionBar
        actionBar?.let {
            with(it) {
                setTitle(title)
            }
        }
    }

    private fun goMultipleRservationConfirm() {
        val time =  adapter.getMutipleTicketTime()
        if ( time != null) {
             goReserveConfirm(time)
        }
    }

    private fun goReserveConfirm(ticketTime: TicketTime) {
        val confirmIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(TICKET, ticket)
                putExtra(TICKET_TIME, ticketTime)
                putExtra(RESERVATION_TYPE, reservation)
                putExtra(PLATE_TIME_CHANGE, timeChange)
            }

        startActivity(confirmIntent)
    }

    override fun bottomAction() {
        super.bottomAction()
        if (isMultiSelectable()) {
            onClickReservationHandler()
        }
    }

}