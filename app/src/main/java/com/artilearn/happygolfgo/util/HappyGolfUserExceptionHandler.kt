package com.artilearn.happygolfgo.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.ui.common.ErrorActivity
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class HappyGolfUserExceptionHandler(
    application: Application,
    private val defaultExceptionHandler: Thread.UncaughtExceptionHandler,
    private val fabricExceptionHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {
    private val TAG = javaClass.simpleName
    private var lastActivity: Activity? = null
    private var activityCount = 0

    init {
        application.registerActivityLifecycleCallbacks(
            object : HappyGolfUserActivityLifecycleCallbacks() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (isSkipActivity(activity)) {
                        return
                    }

                    lastActivity = activity
                }

                override fun onActivityStarted(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount++
                    lastActivity = activity
                }

                override fun onActivityStopped(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount--
                    if (activityCount < 0) {
                        lastActivity = null
                    }
                }
            }
        )
    }

    private fun isSkipActivity(activity: Activity) = activity is ErrorActivity

    override fun uncaughtException(t: Thread, e: Throwable) {
        fabricExceptionHandler.uncaughtException(t, e)
        lastActivity?.run {
            val stringWriter = StringWriter()
            e.printStackTrace(PrintWriter(stringWriter))

            errorLog(TAG, e)

            startErrorActivity(this, stringWriter.toString())
        } ?: defaultExceptionHandler.uncaughtException(t, e)

        Process.killProcess(Process.myPid())
        exitProcess(-1)
    }

    private fun startErrorActivity(activity: Activity, errorText: String) = activity.run {
        val errorActivityIntent = Intent(this, ErrorActivity::class.java)
            .apply {
                putExtra(EXTRA_INTENT, intent)
                putExtra(EXTRA_ERROR_TEXT, errorText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        startActivity(errorActivityIntent)
        finish()
    }
}