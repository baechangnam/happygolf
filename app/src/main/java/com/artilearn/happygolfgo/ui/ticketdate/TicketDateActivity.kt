package com.artilearn.happygolfgo.ui.ticketdate

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.IS_RESERVED_DAY
import com.artilearn.happygolfgo.constants.SELECTION_DATE
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityTicketDateBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TicketDateActivity
    : BaseActivity<ActivityTicketDateBinding>(R.layout.activity_ticket_date) {

    private val viewModel: TicketDateViewModel by viewModel()
    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private val useCalendar by lazy(::TicketDateDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
        binding.ticket = ticket
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@TicketDateActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@TicketDateActivity, Observer {
                showNetworkFail()
            })
            goNext.observe(this@TicketDateActivity, Observer {
                goSelectionTime(it.first, it.second)
            })
            //featue/2022/0216/multiSelection
            goNextForReviewOnly.observe(this@TicketDateActivity, Observer{
                goSelectionTimeForReivewOnly(it.first, it.second)
            })

            appRefresh.observe(this@TicketDateActivity, Observer {
                goRefresh()
            })
            used.observe(this@TicketDateActivity, Observer {
                if (!it) useCalendar.show(supportFragmentManager, useCalendar.tag)
            })
        }
    }

    private fun initView() {
        initToolbar(binding.calendarToolbar.toolbar, getString(R.string.ticketdate_toolbar_title), true)
        ticket?.let {
            viewModel.ticketId = it.id
            viewModel.selectDate(it.type)
        }
    }

    private fun goSelectionTime(ticket: Ticket, date: String) {
        //feature/20220216/multiSelection
        val goActivity = Intent(this, TicketTimeActivity::class.java)
            .apply {
                putExtra(TICKET, ticket)
                putExtra(SELECTION_DATE, date)
            }

        startActivity(goActivity)
    }

    private fun goSelectionTimeForReivewOnly(ticket: Ticket, date:String) {
        val goActivity = Intent(this, TicketTimeActivity::class.java)
            .apply {
                putExtra(TICKET, ticket)
                putExtra(SELECTION_DATE, date)
                putExtra(IS_RESERVED_DAY, true)
            }
        startActivity(goActivity)
    }
}