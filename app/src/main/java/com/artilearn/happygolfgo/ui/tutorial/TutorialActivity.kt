package com.artilearn.happygolfgo.ui.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityTutorialBinding
import com.artilearn.happygolfgo.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TutorialActivity : BaseActivity<ActivityTutorialBinding>(R.layout.activity_tutorial) {

    private val viewModel: TutorialViewModel by viewModel()

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
            btnAnimation.observe(this@TutorialActivity, Observer {
                startAnimation()
            })
            goLogin.observe(this@TutorialActivity, Observer {
                goLogin()
            })
        }
    }

    private fun startAnimation() {
        binding.btnStart.apply {
            animate().alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .duration = 450L
        }
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}