package com.artilearn.happygolfgo.ui.common

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.databinding.ActivityNetworkErrorBinding

class NetworkErrorActivity :
    BaseActivity<ActivityNetworkErrorBinding>(R.layout.activity_network_error) {

    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showEvent()
        btnClickListener()
    }

    private fun showEvent() {
        with(binding) {
            tvTitle.apply {
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .startDelay = 1500L
            }
            tvSubtitle.apply {
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .startDelay = 1500L

            }
            btnReload.apply {
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .rotation(1f)
                    .startDelay = 1500L
            }
        }
    }

    private fun btnClickListener() {
        binding.btnReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }
    }

}