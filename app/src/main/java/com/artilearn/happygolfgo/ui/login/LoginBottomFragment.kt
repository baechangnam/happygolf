package com.artilearn.happygolfgo.ui.login

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.artilearn.happygolfgo.BR
import com.artilearn.happygolfgo.base.BaseBottomSheetFragment
import com.artilearn.happygolfgo.constants.OVERLAP_USER
import com.artilearn.happygolfgo.databinding.FragmentBottomLoginBinding
import com.artilearn.happygolfgo.ui.login.model.LoginOverlap
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_bottom_login.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginBottomFragment : BaseBottomSheetFragment() {
    private val viewModel: LoginViewModel by sharedViewModel()
    private val args by lazy {
        arguments?.getParcelableArrayList<LoginOverlap>(OVERLAP_USER)
    }

    private val layoutParams by lazy {
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)

        args?.let {
            createTextView(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let {
            it.setCanceledOnTouchOutside(false)
            it.setCancelable(true)
        }
    }

    private fun createTextView(items: ArrayList<LoginOverlap>) {
        for (i in items.indices) {
            val tv = TextView(this.context).apply {
                text = items[i].training
                textSize = 30.0f
                setTextColor(Color.WHITE)
                setPadding(0, 4, 0, 4)
                setOnClickListener {
                    viewModel.branchIdClick(items[i])
                    Toast.makeText(this@LoginBottomFragment.context, "$text 선택", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }

            addLayout(tv)
        }
    }

    private fun addLayout(tv: TextView) {
        layoutParams.apply {
            gravity = Gravity.CENTER
            bottomMargin = 10
            topMargin = 10
        }

        tv.layoutParams = layoutParams

        binding.root.linear_layout.addView(tv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                Toast.makeText(this@LoginBottomFragment.context, "사용할 지점을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

}