package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.FragmentBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class ExamBottomSheetDialogFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetDialogBinding
    lateinit var examAdapter: ExamsAdapter

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        examAdapter = ExamsAdapter()
        binding.rvExam.adapter = examAdapter

        examAdapter.setItems(listOf(
            "1월 시험", "2월 시험", "3월 시험", "4월 시험"
        ))

        binding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExamBottomSheetDialogFragment()
    }
}