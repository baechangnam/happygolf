package com.artilearn.happygolfgo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivitySplashBinding
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.ui.tutorial.TutorialActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            goTutorial.observe(this@SplashActivity){
                goTutorial()
            }
            goLogin.observe(this@SplashActivity) {
                goLogin()
            }
            goHome.observe(this@SplashActivity) {
                goHome()
            }
            needUpdate.observe(this@SplashActivity) {
                needReLogin(it)
            }
        }
    }

    private fun needReLogin(text: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("알림")
            .setMessage(text)
            .setPositiveButton("확인") { _, _ ->
                goLogin()
            }
            .setCancelable(false)

        alert.create()
        alert.show()
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun goTutorial() {
        startActivity(Intent(this, TutorialActivity::class.java))
        finish()
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}
