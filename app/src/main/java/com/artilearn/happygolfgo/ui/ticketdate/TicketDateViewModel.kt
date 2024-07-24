package com.artilearn.happygolfgo.ui.ticketdate

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class TicketDateViewModel(
    private val ticketDateRepository: TicketDateRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _dialogCloseSubject: Subject<Unit> = PublishSubject.create()
    private val _dialogNeverSubject: Subject<Unit> = PublishSubject.create()

    private val _eventDayList: MutableLiveData<List<String>> = MutableLiveData()
    val eventDayList: LiveData<List<String>>
        get() = _eventDayList

    private val _availablieList: MutableLiveData<List<String>> = MutableLiveData()
    val availablieList: LiveData<List<String>>
        get() = _availablieList

    private val _goNext: MutableLiveData<Pair<Ticket, String>> = MutableLiveData()
    val goNext: LiveData<Pair<Ticket, String>>
        get() = _goNext
    //feature/2022/0216/multiSelection
    private  val _goNextForReviewOnly : MutableLiveData<Pair<Ticket,String>> = MutableLiveData()
    val goNextForReviewOnly:LiveData<Pair<Ticket,String>>
     get() = _goNextForReviewOnly

    private val _used: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val used: LiveData<Boolean>
        get() = _used

    private val _close: SingleLiveEvent<Unit> = SingleLiveEvent()
    val close: LiveData<Unit>
        get() = _close

    var ticketId: Int? = null

    init {
        compositeDisposable.addAll(
            _dialogCloseSubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe { _close.call() },

            _dialogNeverSubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe { dontNeedDialog(); _close.call() }
        )
    }

    fun selectDate(type: Int) {
        when (type) {
            0 -> selectLessonDate()
            2 -> selectPlateDate()
        }
    }

    private fun selectPlateDate() {
        ticketId?.let {
            ticketDateRepository.selectPlateDate(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({ response ->
                    val body = response.body()
                    with(response) {
                        if (isSuccessful && body != null) {
                            setMyDates(body.data.availableDates, body.data.eventDates)
                        } else {
                            _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                        }
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
    }

    private fun selectLessonDate() {
        ticketId?.let {
            ticketDateRepository.selectLessonDate(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({ response ->
                    val body = response.body()
                    with(response) {
                        if (isSuccessful && body != null) {
                            setMyDates(body.data.availableDates, body.data.eventDates)
                        } else {
                            _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                        }
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
    }

    private fun setMyDates(availablie: List<String>, events: List<String>) {
        _availablieList.value = availablie
        _eventDayList.value = events
        _used.value = ticketDateRepository.getUsed
    }

    fun calendarListener(ticket: Ticket, day: String) {
        _goNext.value = Pair(ticket, day)
    }

    fun calendarListenerForViewOnly(ticket: Ticket, day: String) {
        _goNextForReviewOnly.value = Pair(ticket, day)
    }

    fun onClose() = _dialogCloseSubject.onNext(Unit)
    fun onNeverClose() = _dialogNeverSubject.onNext(Unit)

    private fun dontNeedDialog() {
        ticketDateRepository.getUsed = true
    }

}