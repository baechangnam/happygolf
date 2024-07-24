package com.artilearn.happygolfgo.ui.home.lecture


import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.data.lecture.source.LectureRepository
import com.artilearn.happygolfgo.util.PreferenceManager
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import com.artilearn.happygolfgo.data.LectureHome
import com.artilearn.happygolfgo.data.lecture.LectureBookMarkResponse
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.google.gson.JsonObject


class CommentListViewModel(
    private val repository: LectureRepository) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    val _commentData: MutableLiveData<List<LectureComment>?> = MutableLiveData()

    val commentData: MutableLiveData<List<LectureComment>?>
        get() = _commentData

    val _reg_result: MutableLiveData<LectureBookMarkResponse?> = MutableLiveData()

    val reg_result: MutableLiveData<LectureBookMarkResponse?>
        get() = _reg_result

    init {
      //  getComment(lectNos)
    }

    fun getComment(lectNo : String) {
        repository.getComment(lectNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    _commentData.value = body
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



    fun regComment(map: HashMap<String, String>) {
        repository.regComment(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    _reg_result.value = body
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