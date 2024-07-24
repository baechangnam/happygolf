package com.artilearn.happygolfgo.di

import android.app.Application
import android.app.PendingIntent
import android.util.Log
import com.artilearn.happygolfgo.modules.*
import com.artilearn.happygolfgo.service.RhythmCoachServiceInterface
import com.artilearn.happygolfgo.util.BaseUtils.init
import com.artilearn.happygolfgo.util.HappyGolfUserExceptionHandler
import com.facebook.stetho.Stetho
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.io.IOException
import java.lang.Exception
import java.net.SocketException

class KoinApplication : Application() {

    private val TAG = javaClass.simpleName
    companion object {
        lateinit var mInstance: KoinApplication
    }
    var mRhythmCoachServiceInterface: RhythmCoachServiceInterface? = null

    open fun initRhythmCoachServiceInterface() {
        // version 39(2.2.3) 버그 수정으로 롤백중
        // 2022.02.15 리듬코치 배포 보류 중
//        if (mRhythmCoachServiceInterface == null) {
//            mRhythmCoachServiceInterface = try {
//                RhythmCoachServiceInterface(applicationContext)
//            } catch (e: Exception) {
//                null
//            }
//        }
    }

    override fun onCreate() {
        super.onCreate()
        //TODO: Exception Handling for mInstance and ServiceInterface creation
        mInstance = this
        init(this)
        //TODO:rollback rthythm coach
        // 리듬 코치 배포 보류 중 2022.02.15
        //initRhythmCoachServiceInterface();
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@KoinApplication)
            modules(
                apiModule,
                firebaseModule,
                networkModule,
                localModule,
                remoteModule,
                repositoryModule,
                viewModelModule
            )
        }
        Stetho.initializeWithDefaults(this)
        setCrashHandler()
        rxErrorHandler()
    }

    private fun setCrashHandler() {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            // Crashlytics에서 기본 handler를 호출하기 때문에 이중으로 호출되는것을 막기위해 빈 handler로 설정
        }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        val firebaseExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            HappyGolfUserExceptionHandler(
                this,
                defaultExceptionHandler,
                firebaseExceptionHandler
            )
        )
    }

    private fun rxErrorHandler() {
        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            Log.d(TAG, "Undeliverable exception received, not sure what to do", error)
        }
    }

}