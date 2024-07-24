package com.artilearn.happygolfgo.ui.ticketmanager

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ticketmanager.source.TicketManagerRepository
import com.artilearn.happygolfgo.ui.ticketmanager.model.ExpiredModel
import com.artilearn.happygolfgo.ui.ticketmanager.model.PauseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TicketManagerViewModel(
    private val repository: TicketManagerRepository
) : BaseViewModel() {

    private val _tabPosition: MutableLiveData<Int> = MutableLiveData(0)
    val tabPosition: LiveData<Int>
        get() = _tabPosition

    private val _pauseTicketList: MutableLiveData<List<PauseModel>> = MutableLiveData()
    val pauseTicketList: LiveData<List<PauseModel>>
        get() = _pauseTicketList

    private val _expiredTicketList: MutableLiveData<List<ExpiredModel>> = MutableLiveData()
    val expiredTicketList: LiveData<List<ExpiredModel>>
        get() = _expiredTicketList

    private val _emptyPauseTicket: MutableLiveData<Boolean> = MutableLiveData(false)
    val emptyPauseTicket: LiveData<Boolean>
        get() = _emptyPauseTicket

    private val _emptyExpiredTicket: MutableLiveData<Boolean> = MutableLiveData(false)
    val emptyExpiredTicket: LiveData<Boolean>
        get() = _emptyExpiredTicket

    fun tabItemClick(position: Int) {
        _tabPosition.value = position
    }

    fun getPauseTickets() {
        repository.getPauseTickets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ response ->
                checkPauseTicketList(response)
            }, { t ->
                failError(t)
            }).addTo(compositeDisposable)
    }

    fun getExpiredickets() {
        repository.getExpiredTickets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({
                println("getExpiredickets success: $it")
                checkExpiredTicketList(it)
            }, { t ->
                failError(t)
            }).addTo(compositeDisposable)
    }

    private fun checkPauseTicketList(list: List<PauseModel>) {
        isEmptyPauseTicket(list.isEmpty())
        _pauseTicketList.value = list
    }

    private fun checkExpiredTicketList(list: List<ExpiredModel>) {
        isEmptyExpiredTicket(list.isEmpty())
        _expiredTicketList.value = list
    }

    private fun isEmptyPauseTicket(result: Boolean) = _emptyPauseTicket.postValue(result)

    private fun isEmptyExpiredTicket(result: Boolean) = _emptyExpiredTicket.postValue(result)

    private fun failError(t: Throwable) {
        when (t) {
            is NetworkErrorException -> _networkFail.call()
            is HttpException -> {
                if (t.code() == 419) _appRefresh.call()
                else _serverError.value = "이용권을 불러오는데 실패했습니다 ${t.code()}"
            }
            else -> _serverError.value = "알 수 없는 오류 발생"
        }
    }
}