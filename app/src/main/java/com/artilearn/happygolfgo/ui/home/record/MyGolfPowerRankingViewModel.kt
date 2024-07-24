package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.record.mapper.*
import com.artilearn.happygolfgo.ui.home.record.model.*
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class MyGolfPowerRankingViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {
    private val _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE = "데이타를 불러오는데 실패했습니다."
    private val _ERROR_MESSAGE_UNKNOWN_ERROR  = "알 수 없는 오류 발생"
    private val TAG = javaClass.simpleName
    private val _adapterPageInfo: MutableLiveData<TRMyGolfPowerExamResultPageInfoModel> = MutableLiveData()
    val adapterPageInfo: LiveData<TRMyGolfPowerExamResultPageInfoModel>
        get() =  _adapterPageInfo

    init {
        requestMyGolfPowerExamResultPageInfo()
//        requestSummaryTime()
    }

    private fun requestMyGolfPowerExamResultPageInfo() {
        repository.getMyGolfPowerExamResultPageInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  t -> errorLog(TAG,t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _adapterPageInfo.value = TRMyGolfPowerExamResultPageInfoModelMapper.mapper(body.data.res)
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }
                ,{ t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                        else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                    }
                }).addTo(compositeDisposable)
    }
    /*
    private fun requestSummaryTime() {
        repository.getTrainingManagementSummaryTime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
//            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryTime.value = TRSummaryTimeMapper.mapper(body.data.res);
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                    else -> _serverError.value =  _ERROR_MESSAGE_UNKNOWN_ERROR
                }
            }).addTo(compositeDisposable)
    }
     */
}