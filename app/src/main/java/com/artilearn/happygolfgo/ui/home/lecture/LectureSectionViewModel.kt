package com.artilearn.happygolfgo.ui.home.lecture


import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.lecture.source.LectureRepository
import com.artilearn.happygolfgo.util.PreferenceManager
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import com.artilearn.happygolfgo.data.lecture.LectureDetailResponse
import com.artilearn.happygolfgo.data.lecture.LectureResponse
import com.artilearn.happygolfgo.util.SingleLiveEvent


class LectureSectionViewModel(
    private val repository: LectureRepository,
    private val koinContext: Context,
    private val preferenceManager: PreferenceManager


) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val lecture : MutableLiveData<LectureResponse?> = MutableLiveData()

    private val _addBookmark: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val addBookMark: LiveData<Boolean>
        get() = _addBookmark

    val _lecture: MutableLiveData<LectureResponse?>
        get() =lecture

    private var keyword = ""
    private var sectNo  = ""
    private var page = ""

    init {
       // getLectureList(keyword,sectNo,page)
    }

    fun getLectureList(keyword: String,sectNo: String,page: String,order: String) {
        repository.getLectureList(keyword,sectNo,page,order)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    lecture.value = body
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



    fun addLike(lectNo: String) {
        repository.addBookMark(lectNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    Log.d("myLog", "serverStatus " +body.serverStatus)
                    if(body.serverStatus == "2"){
                        _addBookmark.value =true
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

    fun cancelLike(lectNo: String) {
        repository.cancelBookMark(lectNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    Log.d("myLog", "serverStatus " +body.serverStatus)
                    if(body.serverStatus == "2"){
                        _addBookmark.value =true
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


}