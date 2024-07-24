package com.artilearn.happygolfgo.ui.golfrank

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.data.golfrank.source.GolfRankRepository
import com.artilearn.happygolfgo.ui.golfrank.mapper.GolfRankMapper
import com.artilearn.happygolfgo.ui.golfrank.model.GolfRankModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class GolfRankViewModel(
    private val repository: GolfRankRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _trashSubject: PublishSubject<Unit> = PublishSubject.create()
    private val _defaultTrashSubject: BehaviorSubject<Unit> = BehaviorSubject.createDefault(Unit)

    private val mapper: GolfRankMapper = GolfRankMapper
    private val _globalRanks: SingleLiveEvent<List<GolfRankModel>> = SingleLiveEvent()
    private val _localRanks: SingleLiveEvent<List<GolfRankModel>> = SingleLiveEvent()

    val globalRanks: LiveData<List<GolfRankModel>>
        get() = _globalRanks

    val localRanks: LiveData<List<GolfRankModel>>
        get() = _localRanks

    init {
        val trash = _trashSubject
            .startWith(Unit)
            .share()

        val default = Observable.combineLatest(
            trash, _defaultTrashSubject, BiFunction { _: Unit, _: Unit -> Unit }
        ).replay(1).autoConnect()

        val ranks = default
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .switchMapSingle {
                repository.getRanks()
                    .subscribeOn(Schedulers.io())
            }
            .doOnError { t -> errorLog(TAG, t) }
            .materialize()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading() }
            .share()

        ranks.filter { it.isOnNext }
            .map { it.value }
            .map(mapper::globalMapper)
            .subscribe(_globalRanks::setValue)
            .let(compositeDisposable::add)

        ranks.filter { it.isOnNext }
            .map { it.value }
            .map(mapper::localMapper)
            .subscribe(_localRanks::setValue)
            .let(compositeDisposable::add)

        ranks.map { it.error }
            .filter { true }
            .subscribe { e ->
                when (e) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (e.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }.addTo(compositeDisposable)
    }
}