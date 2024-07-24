package com.artilearn.happygolfgo.ui.tutorial

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class TutorialViewModel(
    private val tutorialRepository: TutorialRepository
) : BaseViewModel() {

    private val _btnAnimation: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _goLogin: SingleLiveEvent<Unit> = SingleLiveEvent()

    val btnAnimation: LiveData<Unit>
        get() = _btnAnimation

    val goLogin: LiveData<Unit>
        get() = _goLogin

    init {
        Completable.create { emitter ->
            emitter.onComplete()
        }
            .delay(1500L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _btnAnimation.call()
            }.addTo(compositeDisposable)
    }

    fun onStartClick() {
        tutorialRepository.tutorial = true
        _goLogin.call()
    }
}