package com.artilearn.happygolfgo.ui.lessonpostlist

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.lessonlog.source.LessonLogRepository
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class LessonPostListViewModel(
    private val lessonLogRepository: LessonLogRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _emptyLessonLog: MutableLiveData<Boolean> = MutableLiveData()
    private val _lessonLog: MutableLiveData<ArrayList<LessonLog>> = MutableLiveData()

    val emptyLessonLog: LiveData<Boolean>
        get() = _emptyLessonLog

    val lessonLog: LiveData<ArrayList<LessonLog>>
        get() = _lessonLog

    init {
        requestLessonPostList()
    }

    private fun requestLessonPostList() {
        lessonLogRepository.lessonLog()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.lessonLogs.isNotEmpty()) {
                        _lessonLog.value = body.data.lessonLogs as ArrayList<LessonLog>
                        hideEmptyLessonLog()
                    } else {
                        showEmptyLessonLog()
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

    private fun showEmptyLessonLog() {
        _emptyLessonLog.value = true
    }

    private fun hideEmptyLessonLog() {
        _emptyLessonLog.value = false
    }
}