package com.artilearn.happygolfgo.base

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.artilearn.happygolfgo.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    protected var _binding: ViewDataBinding? = null
    protected val binding
        get() = _binding!!

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)
}