package com.artilearn.happygolfgo.ui.pwauth

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class PwAuthViewModel(
    private val pwAuthRepository: PwAuthRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private var disposable: Disposable? = null
    private val _phoneNumber = BehaviorSubject.createDefault("")
    private val _authNumber = BehaviorSubject.createDefault("")
    private val _sendButton = PublishSubject.create<Unit>()
    private val _confirmButton = PublishSubject.create<Unit>()
    private val timerTask = Observable.interval(0, 1, TimeUnit.SECONDS)
        .map { seconds -> authTimer.onNext(seconds) }
    private val authTimer = PublishSubject.create<Long>()

    private val _phoneValueType = SingleLiveEvent<PwAuthPhoneType>()
    private val _authValueType = SingleLiveEvent<PwAuthNumberType>()

    val phoneValueType: LiveData<PwAuthPhoneType>
        get() = _phoneValueType

    val authValueType: LiveData<PwAuthNumberType>
        get() = _authValueType

    private var timerState = false
    private var status = false
    private var successPhone = ""

    init {
        bindRx()
    }

    fun phoneOnNext(str: String) {
        _phoneNumber.onNext(str)
    }

    fun authOnNext(str: String) {
        _authNumber.onNext(str)
    }

    fun btnSendClick() {
        _sendButton.onNext(Unit)
    }

    fun btnConfirmClick() {
        _confirmButton.onNext(Unit)
    }

    private fun loginAndNumberCheck(phone: String) {
        when {
            pwAuthRepository.autoLogin -> {
                if (pwAuthRepository.phoneNumber == phone) {
                    authNumber(phone)    
                } else {
                    _serverError.value = "로그인한 핸드폰 번호와 일치하지 않습니다"
                }
            }
            else -> {
                authNumber(phone)
            }
        }
    }

    private fun authNumber(phone: String) {
        val dataModel = GetAuthModel(phone)
        val hashMap = hashMapOf<String, GetAuthModel>()
        hashMap["Data"] = dataModel

        pwAuthRepository.getAuthNumber(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                rxTimerStart()
                _phoneValueType.value = PwAuthPhoneType.SUCCESS
                successPhone = phone
                status = true
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) {
                            _appRefresh.call()
                        } else {
                            when (t.message) {
                                "HTTP 404 Not Found" -> _phoneValueType.value = PwAuthPhoneType.EMPTY_USER
                                "Network Error" -> _phoneValueType.value = PwAuthPhoneType.NETWORK_FAIL
                                else -> _phoneValueType.value = PwAuthPhoneType.FAIL
                            }
                        }
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun confirmAuthNumber() {
        with(_authNumber.value) {
            when {
                isNullOrEmpty() -> _authValueType.value = PwAuthNumberType.EMPTY
                else -> confirmAuthDataModel(this!!)
            }
        }
    }

    private fun confirmAuthDataModel(authNumber: String) {
        if (successPhone == _phoneNumber.value && timerState) {
            val dataModel = ConfirmAuthModel(successPhone, authNumber)
            val hashMap = hashMapOf<String, ConfirmAuthModel>()
            hashMap["Data"] = dataModel

            pwAuthRepository.confirmAuthNumber(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({
                    _authValueType.value = PwAuthNumberType.SUCCESS
                }, { t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) {
                                _appRefresh.call()
                            } else {
                                when (t.message) {
                                    "Network Error" -> _authValueType.value = PwAuthNumberType.NETWORK_FAIL
                                    "HTTP 406 Not Acceptable" -> _authValueType.value = PwAuthNumberType.AUTH_ERROR
                                    else -> _authValueType.value = PwAuthNumberType.FAIL
                                }
                            }
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                }).addTo(compositeDisposable)
        } else if (successPhone != _phoneNumber.value) {
            _authValueType.value = PwAuthNumberType.MODEL_FAIL
        } else {
            status = false
            disposable?.dispose()
            _authValueType.value = PwAuthNumberType.TIMER_OVER
        }
    }

    private fun rxTimerStart() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        disposable = timerTask.observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun sendButtonObservable(): Observable<StringType> {
        return _sendButton.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .map { StringType.PHONE }
    }

    private fun confirmButtonObservable(): Observable<StringType> {
        return _confirmButton.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .map { StringType.AUTH }
    }

    fun getPhoneNumber(): String? {
        return _phoneNumber.value
    }

    private fun bindRx() {
        authTimer.observeOn(AndroidSchedulers.mainThread())
            .subscribe { timerState = it <= 180 }
            .addTo(compositeDisposable)

        Observable.merge(sendButtonObservable(), confirmButtonObservable())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { type ->
                when (type) {
                    StringType.PHONE -> {
                        with(_phoneNumber.value) {
                            when {
                                isNullOrEmpty() -> _phoneValueType.value = PwAuthPhoneType.EMPTY
                                this?.length == 11 -> loginAndNumberCheck(this)
                                else -> _phoneValueType.value = PwAuthPhoneType.LEGNTH_FAIL
                            }
                        }
                    }
                    StringType.AUTH -> {
                        if (status) {
                            confirmAuthNumber()
                        } else {
                            _authValueType.value = PwAuthNumberType.UNCREATE
                        }
                    }
                }
            }
            .addTo(compositeDisposable)
    }

    enum class StringType {
        PHONE,
        AUTH
    }
}
