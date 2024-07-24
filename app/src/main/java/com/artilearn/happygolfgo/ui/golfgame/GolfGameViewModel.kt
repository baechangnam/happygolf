package com.artilearn.happygolfgo.ui.golfgame

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.golfgame.source.GolfGameRepository
import com.artilearn.happygolfgo.ui.golfgame.mapper.GolfGameMapper
import com.artilearn.happygolfgo.ui.golfgame.model.GolfGameModel
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

class GolfGameViewModel(
    private val repository: GolfGameRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _trashSubject: PublishSubject<Unit> = PublishSubject.create()
    private val _isDefaultTrashSubject: BehaviorSubject<Unit> = BehaviorSubject.createDefault(Unit)
    private val _golfModelList: BehaviorSubject<GolfGameModel> = BehaviorSubject.create()
    private val _defaultGolfGameModel: PublishSubject<GolfGameModel> = PublishSubject.create()

    private val _mapper: GolfGameMapper = GolfGameMapper

    private val _golfGame: SingleLiveEvent<List<GolfGameModel>> = SingleLiveEvent()

    val golfGame: LiveData<List<GolfGameModel>>
        get() = _golfGame

    init {
        val trash = _trashSubject
            .startWith(Unit)
            .share()

        val trashSubject = Observable.combineLatest(
            trash, _isDefaultTrashSubject, BiFunction { _: Unit, _: Unit -> Unit }
        ).replay(1).autoConnect()

        val golfGameData = trashSubject
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .switchMapSingle {
                repository.getGolfGames()
                    .subscribeOn(Schedulers.io())
            }
            .doOnError { t -> errorLog(TAG, t) }
            .materialize()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading() }
            .share()

        golfGameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data }
            .map(_mapper::driverMapper)
            .subscribe(_defaultGolfGameModel::onNext)
            .let(compositeDisposable::add)

        golfGameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data }
            .map(_mapper::woodMapper)
            .subscribe(_defaultGolfGameModel::onNext)
            .let(compositeDisposable::add)

        golfGameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data }
            .map(_mapper::ironMapper)
            .subscribe(_defaultGolfGameModel::onNext)
            .let(compositeDisposable::add)

        golfGameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data }
            .map(_mapper::shortMapper)
            .subscribe(_defaultGolfGameModel::onNext)
            .let(compositeDisposable::add)

        golfGameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data }
            .map(_mapper::puttMapper)
            .subscribe(_defaultGolfGameModel::onNext)
            .let(compositeDisposable::add)

        _defaultGolfGameModel.observeOn(AndroidSchedulers.mainThread())
            .subscribe(_golfModelList::onNext)
            .addTo(compositeDisposable)

        _golfModelList
            .buffer(5)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_golfGame::setValue)
            .addTo(compositeDisposable)

        golfGameData.map { it.error }
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