package com.artilearn.happygolfgo.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.FragmentDeveloperOptionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeveloperOptionFragment : BottomSheetDialogFragment() {

    private val viewModel: DeveloperOptionViewModel by viewModel()
    private lateinit var binding: FragmentDeveloperOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_developer_option,
            container,
            false
        )

        initBinding()
        initViewModelCallback()

        return binding.root
    }

    private fun initBinding() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            prodution.observe(this@DeveloperOptionFragment, Observer {
                closeDialog()
            })
            test.observe(this@DeveloperOptionFragment, Observer {
                closeDialog()
            })
            developer.observe(this@DeveloperOptionFragment, Observer {
                closeDialog()
            })
        }
    }

    private fun closeDialog() {
        dismiss()
    }

}