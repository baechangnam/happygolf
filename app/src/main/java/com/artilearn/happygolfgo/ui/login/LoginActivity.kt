package com.artilearn.happygolfgo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.base.BaseLoadingFragment
import com.artilearn.happygolfgo.constants.LOADING
import com.artilearn.happygolfgo.constants.OVERLAP_USER
import com.artilearn.happygolfgo.databinding.ActivityLoginBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.common.DeveloperOptionFragment
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.login.model.LoginOverlap
import com.artilearn.happygolfgo.ui.pwauth.PwAuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel: LoginViewModel by viewModel()
    private val loginBottomView by lazy(::LoginBottomFragment)
    private val devOptionView by lazy(::DeveloperOptionFragment)
    private val loadingView by lazy { BaseLoadingFragment(R.layout.fragment_loading) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initTextListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@LoginActivity) {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            }
            serverError.observe(this@LoginActivity) {
                showToast(ToastType.ERROR, it)
            }
            forgetPassword.observe(this@LoginActivity) {
                goPasswordAuth()
            }
            developerOption.observe(this@LoginActivity) {
                devOptionView.show(supportFragmentManager, devOptionView.tag)
            }
            loginType.observe(this@LoginActivity) {
                when (loginType.value) {
                    LoginViewModel.LoginType.EMPTY_PHONE -> showEmptyPhone()
                    LoginViewModel.LoginType.EMPTY_PASSWORD -> showEmptyPassword()
                    LoginViewModel.LoginType.NOT_FOUND_USER -> showEmptyUser()
                    LoginViewModel.LoginType.FAIL_PASSWORD -> showFailPassword()
                    LoginViewModel.LoginType.SUCCESS -> goHome()
                    LoginViewModel.LoginType.SHOW_LOADING -> loadingView.show(supportFragmentManager, LOADING)
                    LoginViewModel.LoginType.HIDE_LOADING -> loadingView.dismissAllowingStateLoss()
                    else -> goHome() //when expression must be exhaustive
                }
            }
            appRefresh.observe(this@LoginActivity) {
                goRefresh()
            }
            overlapUser.observe(this@LoginActivity) { users ->
                println("ok!: $users")
                setOverlapUser(users)
            }
        }
    }

    private fun initTextListener() {
        with(binding) {
            etPhone.addTextChangedListener { viewModel.phoneOnNext(it.toString()) }
            etPw.addTextChangedListener { viewModel.passwordOnNext(it.toString()) }
        }
    }

    private fun setOverlapUser(item: List<LoginOverlap>) {
        val bundle = Bundle()
        val newItem = ArrayList<LoginOverlap>()
        newItem.addAll(item)
        bundle.putParcelableArrayList(OVERLAP_USER, newItem)
        loginBottomView.arguments = bundle
        loginBottomView.show(supportFragmentManager, loginBottomView.tag)
    }

    private fun showEmptyUser() {
        binding.etPhone.error = getString(R.string.login_empty_user)
    }

    private fun showEmptyPhone() {
        binding.etPhone.error = getString(R.string.login_empth_phone)
    }

    private fun showEmptyPassword() {
        binding.etPw.error = getString(R.string.login_empty_password)
    }

    private fun showFailPassword() {
        binding.etPw.error = getString(R.string.login_fail_password)
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun goPasswordAuth() {
        startActivity(Intent(this, PwAuthActivity::class.java))
    }

}