package com.artilearn.happygolfgo.ui.pwreset

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.constants.PASSWORD_REGEX
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.util.regex.Pattern

class PwResetViewModel(
    private val pwrestRepository: PwrestRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val currentPhone = BehaviorSubject.create<String>()
    private val _bottomClick = PublishSubject.create<Unit>()
    private val _password = BehaviorSubject.create<String>()
    private val _confirm = BehaviorSubject.create<String>()
    private val _check = Observable.combineLatest(
        passwordValidator(), confirmValidator(), BiFunction { p: Boolean, c: Boolean -> p to c }
    )
        .map { (p, c) -> p && c }

    private val _passwordValue = SingleLiveEvent<Boolean>()
    private val _confirmValue = SingleLiveEvent<Boolean>()
    private val _button = SingleLiveEvent<Boolean>()
    private val _success = SingleLiveEvent<Unit>()

    val passwordValue: LiveData<Boolean>
        get() = _passwordValue

    val confirmValue: LiveData<Boolean>
        get() = _confirmValue

    val button: LiveData<Boolean>
        get() = _button

    val success: LiveData<Unit>
        get() = _success

    init {
        bindRx()
    }

    fun passwordOnNext(password: String) {
        _password.onNext(password)
    }

    fun confirmOnNext(confirm: String) {
        _confirm.onNext(confirm)
    }

    fun bottomClick() {
        _bottomClick.onNext(Unit)
    }

    fun setCurrentPhoneNumber(phoneNumber: String) {
        currentPhone.onNext(phoneNumber)
    }

    private fun passwordValidator(): Observable<Boolean> {
//        return _password.map { p -> Pattern.matches(PASSWORD_REGEX, p) }
//            .doOnNext { _passwordValue.value = it }
        return _password.map { p -> p.length >= 4 }
            .doOnNext { _passwordValue.value = it }
    }

    private fun confirmValidator(): Observable<Boolean> {
        return Observable.combineLatest(
            _password, _confirm, BiFunction { p: String, c: String -> p == c }
        )
            .doOnNext { _confirmValue.value = it }
    }

    private fun bindRx() {
        _check.observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(compositeDisposable)

        _bottomClick.withLatestFrom(_check, BiFunction { _: Unit, x: Boolean -> x })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                if (result) putDataModel()
            }
            .addTo(compositeDisposable)
    }

    private fun putDataModel() {
        val dataModel = PwrestModel(currentPhone.value!!, _password.value!!)
        val hashMap = hashMapOf<String, PwrestModel>()
        hashMap["Data"] = dataModel

        pwrestRepository.changePassword(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _success.call()
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "비밀번호를 변경하는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

}