package com.artilearn.happygolfgo.ui.myinfo

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepository
import com.artilearn.happygolfgo.enummodel.UploadProfileType
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class MyInfoViewModel(
    private val myinfoRepository: MyInfoRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    val nickname = MutableLiveData("")

    private val _emptyNickName: MutableLiveData<Unit> = MutableLiveData()
    private val _failNickName: MutableLiveData<Unit> = MutableLiveData()
    private val _localName: MutableLiveData<String> = MutableLiveData()
    private val _localNickName: MutableLiveData<String> = MutableLiveData()
    private val _upload: MutableLiveData<UploadProfileType> = MutableLiveData()

    val emptyNickName: LiveData<Unit>
        get() = _emptyNickName

    val failNickName: LiveData<Unit>
        get() = _failNickName

    val localName: LiveData<String>
        get() = _localName

    val localNickName: LiveData<String>
        get() = _localNickName

    val upload: LiveData<UploadProfileType>
        get() = _upload

    private var currentNickName = ""

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        _localNickName.value = myinfoRepository.nickname
        _localName.value = myinfoRepository.name
    }

    fun onConfirm() {
        currentNickName = nickname.value.toString()

        when {
            currentNickName.isEmpty() -> _emptyNickName.value = Unit
            currentNickName.length < 2
                    || currentNickName.length > 8 -> _failNickName.value = Unit
            else -> uploadUserProfile()
        }
    }

    private fun uploadUserProfile() {
        val myInfoModel = MyInfoModel(currentNickName)
        val hashMap = hashMapOf<String, MyInfoModel>()
        hashMap["Data"] = myInfoModel

        myinfoRepository.uploadProfile(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _upload.value = UploadProfileType.SUCCESS
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _upload.value = UploadProfileType.NETWORK_FAIL
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _upload.value = UploadProfileType.UNKNOW_ERROR
                    }
                    else -> _upload.value = UploadProfileType.UNKNOW_ERROR
                }
            }).addTo(compositeDisposable)
    }
}