package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_LIST
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.FragmentReservePlateBinding
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReservePlateFragment
    : BaseFragment<FragmentReservePlateBinding, ReservePlateViewModel>
        (R.layout.fragment_reserve_plate) {

    override val viewModel: ReservePlateViewModel by viewModel()
    private lateinit var adapter: ReserveListAdapter
    private val cancel = "cancel_plate"

    companion object {

        fun newInstance(reservation: ArrayList<ReserveList>): ReservePlateFragment {
            val args = Bundle()
            args.putParcelableArrayList(RESERVATION_LIST, reservation)
            val fragment = ReservePlateFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        initAdapter()

        val reservationPlate = arguments?.getParcelableArrayList<ReserveList>(RESERVATION_LIST)
        with(reservationPlate) {
            if (this?.size != 0) {
                emptyPlateView(false)
                setReservationPlate(this!!)
            } else {
                emptyPlateView(true)
            }
        }
    }

    private fun initAdapter() {
        adapter = ReserveListAdapter { plateItem ->
            goReservationConfirm(plateItem)
        }

        binding.recyclerview.adapter = adapter
    }

    private fun setReservationPlate(item: List<ReserveList>) {
        with(adapter) {
            clear()
            setItems(item)
        }
    }

    private fun emptyPlateView(stauts: Boolean) {
        binding.emptyPlateLayout.visibility = if (stauts) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun goReservationConfirm(item: ReserveList) {
        //doneTODO: 예약 리스트 상세로 이동
        val confirmIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(PLATE_ID, item.id)
                putExtra(RESERVATION_TYPE, cancel)
            }
        startActivity(confirmIntent)
    }
}