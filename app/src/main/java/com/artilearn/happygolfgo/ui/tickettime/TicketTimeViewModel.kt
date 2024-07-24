package com.artilearn.happygolfgo.ui.tickettime

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TicketTimeViewModel(
    private val ticketTimeRepository: TicketTimeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _ticketTime: SingleLiveEvent<ArrayList<TicketTime>> = SingleLiveEvent()
    private val _emptyTicketTime: SingleLiveEvent<Boolean> = SingleLiveEvent()

    private val _buttonClickable: SingleLiveEvent<Unit> = SingleLiveEvent()

    val ticketTime: LiveData<ArrayList<TicketTime>>
        get() = _ticketTime

    val emptyTicketTime: LiveData<Boolean>
        get() = _emptyTicketTime

//    val buttonClickable: LiveData<Unit>
//        get() = _buttonClickable

    fun selectTime(type: Int, date: String, id: Int) {
        when (type) {
            0 -> selectTicketLessonTime(date, id)
            2 -> selectTicketPlateTime(date, id)
        }
    }

    private fun selectTicketPlateTime(date: String, id: Int) {
        ticketTimeRepository.selectPlateTime(date, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.availableTimes.isNotEmpty()) {
                        hideEmptyTicketTimeView()
                        _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
                    } else {
                        showEmptyTicketTimeView()
                    }
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun selectTicketLessonTime(date: String, id: Int) {
        ticketTimeRepository.selectLessonTime(date, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.availableTimes.isNotEmpty()) {
                        hideEmptyTicketTimeView()
                        _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
                    } else {
                        showEmptyTicketTimeView()
                    }
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun showEmptyTicketTimeView() {
        _emptyTicketTime.value = true
    }

    private fun hideEmptyTicketTimeView() {
        _emptyTicketTime.value = false
    }
}