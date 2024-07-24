package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.artilearn.happygolfgo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExamGuideFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exam_guide, container, false)
        view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            dismissAllowingStateLoss()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExamGuideFragment()
    }
}