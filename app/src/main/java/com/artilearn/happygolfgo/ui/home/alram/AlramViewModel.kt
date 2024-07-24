package com.artilearn.happygolfgo.ui.home.alram

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class AlramViewModel(
    private val repository: AlramRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _deleteSubject: BehaviorSubject<Alram> = BehaviorSubject.create()
    private val _deleteAllSubject: PublishSubject<Unit> = PublishSubject.create()

    private val _alramMessages: MutableLiveData<List<Alram>> = MutableLiveData()
    private val _deleteAlram: SingleLiveEvent<Alram> = SingleLiveEvent()
    private val _emptyList: SingleLiveEvent<List<Alram>> = SingleLiveEvent()

    val alramMessages: LiveData<List<Alram>>
        get() = _alramMessages

    val deleteAlram: LiveData<Alram>
        get() = _deleteAlram

    val emptyList: LiveData<List<Alram>>
        get() = _emptyList

    val checkAlramMessage: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        requestMessage()

        _deleteSubject.subscribe { item -> requestDelete(item) }
            .addTo(compositeDisposable)

        _deleteAllSubject.subscribe { requestDeleteAll() }
            .addTo(compositeDisposable)
    }

    private fun requestMessage() {
        repository.getAlramMessage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    if (body.data.notifications.isNotEmpty()) {
                        _alramMessages.value = body.data.notifications
                        checkAlramMessage(_alramMessages.value!!)
                    } else {
                        checkAlramMessage(emptyList())
                    }
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    fun checkAlramMessage(alrams: List<Alram>) {
        checkAlramMessage.value = alrams.isEmpty()
    }

    fun deleteToAlram(item: Alram) = _deleteSubject.onNext(item)
    fun deleteAll() = _deleteAllSubject.onNext(Unit)

    private fun requestDeleteAll() {
        repository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _emptyList.value = emptyList()
                checkAlramMessage(emptyList())
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "알림 메세지를 삭제하는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun requestDelete(item: Alram) {
        repository.deleteAlram(item.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _deleteAlram.value = item
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "알림 메세지를 삭제하는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

}