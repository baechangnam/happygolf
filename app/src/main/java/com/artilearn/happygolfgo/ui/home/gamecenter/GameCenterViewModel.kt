package com.artilearn.happygolfgo.ui.home.gamecenter

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.gamecenter.mapper.AveragesMapper
import com.artilearn.happygolfgo.ui.home.gamecenter.mapper.RankMapper
import com.artilearn.happygolfgo.ui.home.gamecenter.mapper.SumaryMapper
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCAveragesModel
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCRankModel
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCSumaryModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.merge
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class GameCenterViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _onRankDetailButton: Subject<ButtonType> = PublishSubject.create()
    private val _onGameDetailButton: Subject<ButtonType> = PublishSubject.create()
    private val _isGameDataSubject: PublishSubject<Unit> = PublishSubject.create()
    private val _isDefaultGameDataSubject: BehaviorSubject<Unit> = BehaviorSubject.createDefault(Unit)

    private val _sumaryMapper: SumaryMapper = SumaryMapper
    private val _averagesMapper: AveragesMapper = AveragesMapper
    private val _rankMapper: RankMapper = RankMapper

    private val _button: SingleLiveEvent<ButtonType> = SingleLiveEvent()
    private val _user: MutableLiveData<GameCenterResponse.GameCenterUser> = MutableLiveData()
    private val _averages: MutableLiveData<GCAveragesModel> = MutableLiveData()
    private val _sumary: MutableLiveData<GCSumaryModel> = MutableLiveData()
    private val _rank: MutableLiveData<List<GCRankModel>> = MutableLiveData()

    val button: LiveData<ButtonType>
        get() = _button

    val user: LiveData<GameCenterResponse.GameCenterUser>
        get() = _user

    val averages: LiveData<GCAveragesModel>
        get() = _averages

    val sumary: LiveData<GCSumaryModel>
        get() = _sumary

    val rank: LiveData<List<GCRankModel>>
        get() = _rank

    init {
        listOf(rankDetail(), gameDetail())
            .merge()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_button::setValue)
            .addTo(compositeDisposable)

        val emptySubject = _isGameDataSubject
            .startWith(Unit)
            .share()

        val defaultSubject = Observable.combineLatest(
            emptySubject, _isDefaultGameDataSubject, BiFunction { _: Unit, _: Unit -> Unit }
        ).replay(1).autoConnect()

        val gameData = defaultSubject
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .switchMapSingle {
                repository.getGames()
                    .subscribeOn(Schedulers.io())
            }
            .doOnError { t -> errorLog(TAG, t) }
            .materialize()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading() }
            .share()

        gameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data.user }
            .subscribe(_user::setValue)
            .let(compositeDisposable::add)

        gameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data.sumary }
            .map(_sumaryMapper::mapper)
            .subscribe(_sumary::setValue)
            .let(compositeDisposable::add)

        gameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data.rank }
            .map(_rankMapper::mapper)
            .subscribe(_rank::setValue)
            .let(compositeDisposable::add)

        gameData.filter { it.isOnNext }
            .map { it.value }
            .map { it.data.averages }
            .map(_averagesMapper::mapper)
            .subscribe(_averages::setValue)
            .let(compositeDisposable::add)

        gameData.map { it.error }
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

    private fun rankDetail(): Observable<ButtonType> {
        return _onRankDetailButton.throttleFirst(1000, TimeUnit.MILLISECONDS)
    }

    private fun gameDetail(): Observable<ButtonType> {
        return _onGameDetailButton.throttleFirst(1000, TimeUnit.MILLISECONDS)
    }

    fun onRankDetail() {
        _onRankDetailButton.onNext(ButtonType.RANK)
    }

    fun onGameDetail() {
        _onGameDetailButton.onNext(ButtonType.GAME)
    }

    enum class ButtonType {
        RANK,
        GAME
    }
}