package com.artilearn.happygolfgo.ui.splash

import android.content.Context
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.artilearn.happygolfgo.util.getAndroidID
import com.artilearn.happygolfgo.util.getWidevineID
import com.artilearn.happygolfgo.util.notificationCheck
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val splashRepository: SplashRepository,
    private val koinContext: Context
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _goHome: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _goLogin: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _goTutorial: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _needUpdate = SingleLiveEvent<String>()

    val goHome: LiveData<Unit>
        get() = _goHome

    val goLogin: LiveData<Unit>
        get() = _goLogin

    val goTutorial: LiveData<Unit>
        get() = _goTutorial

    val needUpdate: LiveData<String>
        get() = _needUpdate

    init {
        val token = splashRepository.fcmToken
        if (token.isNullOrEmpty()) {
            getFcmToken()
        }
        splash()
    }

    private fun splash() {
        Completable.create { emitter ->
            emitter.onComplete()
        }
            .delay(1200L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                splashStatus()
            }.addTo(compositeDisposable)
    }

    private fun splashStatus() {
        if (splashRepository.tutorial) {
            if (splashRepository.autoLogin) {
                confirmClientSecretKey()
            } else {
                _goLogin.call()
            }
        } else {
            _goTutorial.call()
        }
    }

    private fun confirmClientSecretKey() {
        if (splashRepository.clientSecretKey.isNotEmpty()) {
            userSynchronized()
        } else {
            _goLogin.call()
        }
    }

    private fun selectAccessToken() {
        val uuid = getWidevineID()
        val dataModel = SplashTokenModel(splashRepository.clientSecretKey, uuid)
        val hashMap = hashMapOf<String, SplashTokenModel>()
        hashMap["Data"] = dataModel

        splashRepository.getAccessToken(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _goHome.call()
            }, { t ->
                _goHome.call()
            }).addTo(compositeDisposable)
    }

    private fun userSynchronized() {
        val userPushCheck = notificationCheck(koinContext)
        val id = getAndroidID(koinContext)
        val uuid = getWidevineID()

        val dataModel = SynchronizedDataModel(
            splashRepository.clientSecretKey,
            splashRepository.fcmToken ?: "",
            userPushCheck,
            uuid,
            id
        )

        val hashMap = hashMapOf<String, SynchronizedDataModel>()
        hashMap["Data"] = dataModel

        splashRepository.userSynchronized(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                if (it.code() == 200) {
                    selectAccessToken()
                } else {
                    //deleteUser()
                    splashRepository.deleteUser()
                    _goLogin.call()
                }
            }, {
                selectAccessToken()
            }).addTo(compositeDisposable)
    }

//    private fun deleteUser() {
//        splashRepository.deleteUser()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                _needUpdate.value = "최신 업데이트와 보안상의 이유로 다시 한번 로그인 부탁드립니다"
//            }, {
//                _goLogin.call()
//            }).addTo(compositeDisposable)
//    }

    private fun getFcmToken() {
        splashRepository.getFcmToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
            .addTo(compositeDisposable)
    }

}