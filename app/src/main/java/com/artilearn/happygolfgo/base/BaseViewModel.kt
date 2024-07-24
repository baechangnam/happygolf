package com.artilearn.happygolfgo.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    protected val _serverError = SingleLiveEvent<String>()
    val serverError: LiveData<String>
        get() = _serverError

    protected val _networkFail = SingleLiveEvent<Unit>()
    val networkFail: LiveData<Unit>
        get() = _networkFail

    protected val _appRefresh = SingleLiveEvent<Unit>()
    val appRefresh: LiveData<Unit>
        get() = _appRefresh

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    companion object {
        const val CONSTANT_IN_COMPANION_OBJECT = "constant at in companion object"
        const val CONSTANT_ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE = "데이타를 불러오는데 실패했습니다."
        const val CONSTANT_ERROR_MESSAGE_UNKNOWN_ERROR  = "알 수 없는 오류 발생"
    }
}