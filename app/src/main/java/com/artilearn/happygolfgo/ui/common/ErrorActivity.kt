package com.artilearn.happygolfgo.ui.common

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.databinding.ActivityErrorBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics

class ErrorActivity : BaseActivity<ActivityErrorBinding>(R.layout.activity_error) {
    private val TAG = javaClass.simpleName
    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }
    private lateinit var versionName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        versionName = findViewById(R.id.tv_version)

        showEvent()
        initClickListener()

        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            versionName.text = packageInfo.versionName
            throw  RuntimeException(TAG)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            FirebaseCrashlytics.getInstance().log("""
                errorMsg: $errorText
                e: $e
                intent: $lastActivityIntent
            """.trimIndent()
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun showEvent() {
        with(binding) {
            tvErrorTitle.apply {
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .startDelay = 1500L
            }
            tvErrorMessage.apply {
                this.text = errorText
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
                    .startDelay = 1500L
            }
        }
    }

    private fun initClickListener() {
        binding.btnReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }
    }

}