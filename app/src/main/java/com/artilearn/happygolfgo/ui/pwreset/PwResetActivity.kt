package com.artilearn.happygolfgo.ui.pwreset

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.PHONE_NUMBER
import com.artilearn.happygolfgo.databinding.ActivityPwResetBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PwResetActivity : BaseActivity<ActivityPwResetBinding>(R.layout.activity_pw_reset) {

    private val viewModel: PwResetViewModel by viewModel()
    private val phoneNumber by lazy { intent.getStringExtra(PHONE_NUMBER) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initTextListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            passwordValue.observe(this@PwResetActivity, Observer {
                passwordValidator(it)
            })
            confirmValue.observe(this@PwResetActivity, Observer {
                confirmValidator(it)
            })
            networkFail.observe(this@PwResetActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@PwResetActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            success.observe(this@PwResetActivity, Observer {
                showToast(ToastType.SUCCESS, getString(R.string.pwreset_change_success))
                finish()
            })
            appRefresh.observe(this@PwResetActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        with(binding) {
            initToolbar(pwresetToolbar.toolbar, getString(R.string.pwreset_toolbar_title), true)
            initBottomView(pwresetBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
        }

        phoneNumber?.let { viewModel.setCurrentPhoneNumber(it) }
    }

    private fun initTextListener() {
        with(binding) {
            etPw.addTextChangedListener { viewModel.passwordOnNext(it.toString()) }
            etPwConfirm.addTextChangedListener { viewModel.confirmOnNext(it.toString()) }
        }
    }

    private fun passwordValidator(result: Boolean) {
        with(binding) {
            etPw.error = if (result) {
                null
            } else {
                getString(R.string.pwreset_input_fail_password)
            }
        }
    }

    private fun confirmValidator(result: Boolean) {
        with(binding) {
            etPwConfirm.error = if (result) {
                null
            } else {
                getString(R.string.pwreset_fail_password)
            }
        }
    }

    override fun bottomAction() {
        super.bottomAction()
        viewModel.bottomClick()
    }

}