package com.artilearn.happygolfgo.ui.ticketmanager

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityTicketManagerBinding
import com.artilearn.happygolfgo.ui.ticketmanager.expired.FragmentExpiredTicket
import com.artilearn.happygolfgo.ui.ticketmanager.pause.FragmentPauseTicket
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketManagerActivity : BaseActivity<ActivityTicketManagerBinding>(
    R.layout.activity_ticket_manager
) {

    private val viewModel: TicketManagerViewModel by viewModel()
    private val useFragment by lazy(::FragmentPauseTicket)
    private val overFragment by lazy(::FragmentExpiredTicket)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        initBinding()
        initViewModelObserving()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_edit_in, R.anim.slide_edit_out)
    }

    override fun onDestroy() {
        overridePendingTransition(R.anim.slide_edit_in, R.anim.slide_edit_out)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBinding()  {
        binding.viewModel = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            tabPosition.observe(this@TicketManagerActivity, Observer { position ->
                initFragment(position)
            })
        }
    }

    private fun initFragment(position: Int) {
        when (position) {
            0 -> setFragment(useFragment)
            1 -> setFragment(overFragment)
            else -> throw IndexOutOfBoundsException("Not Found TicketManager Position: $position")
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            replace(R.id.frame_layout, fragment).commit()
        }
    }

}