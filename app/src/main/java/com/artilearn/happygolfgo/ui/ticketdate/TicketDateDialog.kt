package com.artilearn.happygolfgo.ui.ticketdate

import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseDialogFragment
import com.artilearn.happygolfgo.databinding.DialogTicketdateBinding
import com.artilearn.happygolfgo.util.ext.setDialogSize
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TicketDateDialog : BaseDialogFragment<DialogTicketdateBinding, TicketDateViewModel>(
    R.layout.dialog_ticketdate
) {
    override val viewModel: TicketDateViewModel by sharedViewModel()

    override fun init() {
        with(viewModel) {
            close.observe(this@TicketDateDialog, Observer {
                dismiss()
            })
        }
    }

    override fun onResume() {
        super.onResume()

        dialog?.let {
            it.setCanceledOnTouchOutside(false)
            requireActivity().setDialogSize(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.lottieAnim.cancelAnimation()
    }

}