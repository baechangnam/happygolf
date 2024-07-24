package com.artilearn.happygolfgo.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepository
import com.artilearn.happygolfgo.data.remote.retrofit.RetrofitApi
import com.artilearn.happygolfgo.modules.apiModule
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Single
import java.util.*

class DeveloperOptionViewModel(
    private val developerOptionRepository: DeveloperOptionRepository
) : BaseViewModel() {

    private val _prodution = SingleLiveEvent<String>()
    private val _test = SingleLiveEvent<String>()
    private val _developer = SingleLiveEvent<String>()

    val prodution: LiveData<String>
        get() = _prodution

    val test: LiveData<String>
        get() = _test

    val developer: LiveData<String>
        get() = _developer

    fun onProdution() {
        developerOptionRepository.environmentMode = "xxxxx"
        _prodution.value = "xxxxx"
    }

    fun onTest() {
        developerOptionRepository.environmentMode = "xxxxx"
        _test.value = "xxxxx"
    }

    fun onDeveloper() {
        developerOptionRepository.environmentMode = "xxxxx"
        _developer.value = "xxxxx"
    }
}