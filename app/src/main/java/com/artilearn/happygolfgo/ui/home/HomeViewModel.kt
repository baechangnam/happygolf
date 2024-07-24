package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class HomeViewModel : BaseViewModel() {
    private val backPressed: Subject<Unit> = PublishSubject.create()
    private val fcmTypeSubject: Subject<String> = BehaviorSubject.create()
    private val _bottomSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("예약")

    private val _backPressedEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val backPressedEvent: LiveData<Unit>
        get() = _backPressedEvent

    private val _eventType: SingleLiveEvent<FcmTypeMoveModel> = SingleLiveEvent()
    val eventType: LiveData<FcmTypeMoveModel>
        get() = _eventType

    private val _bottom: SingleLiveEvent<String> = SingleLiveEvent()
    val bottom: LiveData<String>
        get() = _bottom

    init {
        rxBind()
    }

    fun fcmOnNext(type: String) {
        fcmTypeSubject.onNext(type)
    }

    private fun rxBind() {
        _bottomSubject.distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_bottom::setValue)
            .addTo(compositeDisposable)

        backPressed.observeOn(AndroidSchedulers.mainThread())
            .subscribe { _backPressedEvent.call() }
            .addTo(compositeDisposable)

        fcmTypeSubject.delay(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    "3" -> _eventType.value = FcmTypeMoveModel.PLATE
                    "4" -> _eventType.value = FcmTypeMoveModel.LESSON
                    "6" -> _eventType.value = FcmTypeMoveModel.LOG
                }
            }.addTo(compositeDisposable)
    }

    fun onNextBottomEvent(name: String) = _bottomSubject.onNext(name)
    fun backPressed() = backPressed.onNext(Unit)

    fun moveToTheTab(name: String)  =  _bottomSubject.onNext(name)

    enum class FcmTypeMoveModel {
        PLATE,
        LESSON,
        LOG
    }

}