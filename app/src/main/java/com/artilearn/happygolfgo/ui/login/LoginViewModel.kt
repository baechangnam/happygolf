package com.artilearn.happygolfgo.ui.login

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.ui.golfrank.model.GolfRankModel
import com.artilearn.happygolfgo.ui.login.mapper.LoginOverlapMapper
import com.artilearn.happygolfgo.ui.login.model.LoginOverlap
import com.artilearn.happygolfgo.util.Qnintuple
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.artilearn.happygolfgo.util.getAndroidID
import com.artilearn.happygolfgo.util.getWidevineID
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit


class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val koinContext: Context
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _phone = BehaviorSubject.create<String>()
    private val _password = BehaviorSubject.create<String>()
    private val _passwordForget = PublishSubject.create<ButtonClickType>()
    private val _developerServer = PublishSubject.create<Unit>()
    private val _login = PublishSubject.create<ButtonClickType>()

    private val mapper: LoginOverlapMapper = LoginOverlapMapper
    private val _forgetPassword = SingleLiveEvent<Unit>()
    private val _developerOption: MutableLiveData<Unit> = MutableLiveData()
    private val _loginType: SingleLiveEvent<LoginType> = SingleLiveEvent()
    private val _overlapUser: SingleLiveEvent<List<LoginOverlap>> = SingleLiveEvent()

    val forgetPassword: LiveData<Unit>
        get() = _forgetPassword

    val developerOption: LiveData<Unit>
        get() = _developerOption

    val loginType: LiveData<LoginType>
        get() = _loginType

    val overlapUser: LiveData<List<LoginOverlap>>
        get() = _overlapUser

    init {
        bindRx()
    }

    fun phoneOnNext(phone: String) {
        _phone.onNext(phone)
    }

    fun passwordOnNext(password: String) {
        _password.onNext(password)
    }

    fun onLoginClick() {
        _login.onNext(ButtonClickType.LOGIN)
    }

    fun onPasswordForgetClick() {
        _passwordForget.onNext(ButtonClickType.FORGET_PASSWORD)
    }

    fun onDeveloperOption() {
        _developerServer.onNext(Unit)
    }

    private fun loginClickObservable(): Observable<ButtonClickType> {
        return _login.throttleFirst(1000, TimeUnit.MILLISECONDS)
    }

    private fun passwordForgetObservable(): Observable<ButtonClickType> {
        return _passwordForget.throttleFirst(1000, TimeUnit.MILLISECONDS)
    }

    private fun inputCheck() {
        when {
            _phone.value.isNullOrEmpty() -> _loginType.value = LoginType.EMPTY_PHONE
            _password.value.isNullOrEmpty() -> _loginType.value = LoginType.EMPTY_PASSWORD
            else -> setDataModel(_phone.value!!, _password.value!!, false, null)
        }
    }

    private fun bindRx() {
        Observable.merge(loginClickObservable(), passwordForgetObservable())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { type ->
                when (type) {
                    ButtonClickType.FORGET_PASSWORD -> _forgetPassword.call()
                    ButtonClickType.LOGIN -> inputCheck()
                }
            }.addTo(compositeDisposable)

        _developerServer.map { System.currentTimeMillis() }
            .buffer(5)
            .map {
                val (one, two, three, four, five) = it
                Qnintuple(one, two, three, four, five)
            }
            .filter { (i, ii, iii, iiii, iiiii) ->
                iiiii - i < TimeUnit.SECONDS.toMillis(2)
            }
            .subscribe { _developerOption.value = Unit  }
            .addTo(compositeDisposable)
    }

    private fun setDataModel(phone: String, password: String, overlapResult: Boolean, branchId: Int?) {
        val id = getAndroidID(koinContext)
        val uuid = getWidevineID()
        val dataModel: LoginDataModel

        dataModel = if (overlapResult) {
            LoginDataModel(phone, password, loginRepository.fcmToken, uuid, id, branchId)
        } else {
            LoginDataModel(phone, password, loginRepository.fcmToken, uuid, id)
        }

        val hashMap = hashMapOf<String, LoginDataModel>()
        hashMap["Data"] = dataModel

        requestLogin(hashMap)
    }

    private fun requestLogin(model: HashMap<String, LoginDataModel>) {
        loginRepository.repositoryLogin(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loginType.value = LoginType.SHOW_LOADING }
            .doAfterTerminate { _loginType.value = LoginType.HIDE_LOADING }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                with(it.data.user) {
                    if (overlapUser == null) {
                        _loginType.value = LoginType.SUCCESS
                    } else {
                        _overlapUser.value = mapper.mapper(overlapUser)
                    }
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        when (t.code()) {
                            401 -> _loginType.value = LoginType.FAIL_PASSWORD
                            404 -> _loginType.value = LoginType.NOT_FOUND_USER
                            419 -> _appRefresh.call()
                            else -> _serverError.value = "로그인 실패"
                        }
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    fun branchIdClick(item: LoginOverlap) {
        setDataModel(_phone.value!!, _password.value!!, true, item.branchId)
    }

    enum class ButtonClickType {
        LOGIN,
        FORGET_PASSWORD
    }

    enum class LoginType {
        EMPTY_PHONE,
        EMPTY_PASSWORD,
        NOT_FOUND_USER,
        FAIL_PASSWORD,
        SUCCESS,
        SHOW_LOADING,
        HIDE_LOADING
    }

}