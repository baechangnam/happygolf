package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.record.model.RankingInBranchData
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class GolfRankingInBranchViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private var disposable : Disposable? = null

    var yearMonth: String = ""
    private val _rankingInBranchData:MutableLiveData<ArrayList<RankingInBranchData>> = MutableLiveData()

    val rankingInBranchData : LiveData<ArrayList<RankingInBranchData>>
    get() = _rankingInBranchData

    var title :String = "-"

    init {

    }

    fun request() {
        requestRankingInBranch()
    }

    private fun requestRankingInBranch() {
        disposable?.let {
            if(!it.isDisposed) {
                 it.dispose()
            }
        }
        disposable = repository.getBranchRanking(yearMonth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoading()
            }
            .doAfterTerminate {
                hideLoading()
            }
            .doOnError { error ->
                errorLog(TAG, error)
            }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val rankingInBranchData = RankingInBranchData.map(body.data.res)
                    println(rankingInBranchData)
                    _rankingInBranchData.value = rankingInBranchData
                    body.data.res.title.let { title  ->
                        this.title = title
                    }
                } else {
                    _serverError.value = CONSTANT_ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            },{  e ->
                when(e) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    else -> _serverError.value = CONSTANT_ERROR_MESSAGE_UNKNOWN_ERROR
                }
            })
            .addTo(compositeDisposable)
    }
}