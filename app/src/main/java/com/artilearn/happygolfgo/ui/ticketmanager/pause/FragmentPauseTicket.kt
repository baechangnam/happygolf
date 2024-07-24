package com.artilearn.happygolfgo.ui.ticketmanager.pause

import android.widget.Toast
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentPuaseTicketBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentPauseTicket : BaseFragment<FragmentPuaseTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_puase_ticket
) {

    override val viewModel: TicketManagerViewModel by sharedViewModel()
    private lateinit var adapter: FragmentPauseTicketAdapter

    override fun init() {
        initBinding()
        initViewModelObserving()
        initRecyclerView()

        viewModel.getPauseTickets()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            networkFail.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.WARNNING, "네트워크 환경이 좋지 않습니다")
            })
            serverError.observe(viewLifecycleOwner, Observer { errorMsg ->
                showFragmentToast(ToastType.ERROR, errorMsg)
            })
            appRefresh.observe(viewLifecycleOwner, Observer {
                goRefreshFromFragment()
            })
        }
    }

    private fun initRecyclerView() {
        adapter = FragmentPauseTicketAdapter {
            Toast.makeText(requireActivity(), "$it", Toast.LENGTH_SHORT).show()
        }

        with(binding) {
            recyclerView.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            recyclerView.adapter = adapter
        }
    }

}