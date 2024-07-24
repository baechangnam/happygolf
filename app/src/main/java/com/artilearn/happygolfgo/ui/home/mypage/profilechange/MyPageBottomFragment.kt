package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.BR
import com.artilearn.happygolfgo.base.BaseBottomSheetFragment
import com.artilearn.happygolfgo.constants.BUNDLE_KEY
import com.artilearn.happygolfgo.constants.REQUEST_KEY
import com.artilearn.happygolfgo.databinding.FragmentBottomMypageBinding
import com.artilearn.happygolfgo.ui.home.mypage.MyPageViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyPageBottomFragment : BaseBottomSheetFragment() {

    private val viewModel: MyPageViewModel by sharedViewModel()
    private var flag = false
    private var result = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomMypageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)

        viewModel.bottomState.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { state ->
                flag = true
                result = when (state) {
                    MyPageViewModel.BottomFragmentState.CANCEL -> 0
                    MyPageViewModel.BottomFragmentState.DEFAULT_IMAGE -> 1
                    MyPageViewModel.BottomFragmentState.GALLERY -> 2
                }

                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to result))
            }
            dismiss()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        if (flag) {
//            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to result))
//        }
    }


}