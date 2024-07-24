package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.record.model.RankingInAllBranchData
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class GolfRankingInAllBranchViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {
    private val classNameForLog = javaClass.simpleName
    var yearMonth: String = ""
    private var disposable : Disposable? = null

    private val _rankingAllBranchData:MutableLiveData<ArrayList<RankingInAllBranchData>>
     = MutableLiveData()
    val rankingAllBranchData : LiveData<ArrayList<RankingInAllBranchData>>
    get() = _rankingAllBranchData

    var title:String = ""
    init {

    }

    fun request() {
         requestRankingAllBranch()
    }

   private fun requestRankingAllBranch() {
       disposable?.let {
            if (!it.isDisposed) {
                 it.dispose()
            }
       }
       disposable = repository.getAllBranchRanking(yearMonth)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .doOnSubscribe {
               showLoading()
           }
           .doAfterTerminate {
               hideLoading()
           }
           .doOnError {error ->
               errorLog(classNameForLog, error)
           }
           .subscribe({ response->
               val body = response.body()
               if(response.isSuccessful && body != null) {
                   val  rankingAllBranchData = RankingInAllBranchData.map(body.data.res)
                   _rankingAllBranchData.value = rankingAllBranchData
                   title = body.data.res.title ?: ""
               } else {
                   _serverError.value = CONSTANT_ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
               }
           },{ error->
               when(error) {
                   is NetworkErrorException -> _networkFail.value = Unit
                   else -> _serverError.value = CONSTANT_ERROR_MESSAGE_UNKNOWN_ERROR
               }
           })
           .addTo(compositeDisposable)
   }
}