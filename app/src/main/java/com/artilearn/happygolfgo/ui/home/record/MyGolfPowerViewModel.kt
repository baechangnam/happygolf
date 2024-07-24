package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.record.model.MyRankingBoardData
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MyGolfPowerViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {

    private val _myRankingBoardData: MutableLiveData<MyRankingBoardData> = MutableLiveData()
    var pageIndex:Int? = null
    private val _classNameForLog = javaClass.simpleName
    //Comment: isVisible View.GONE:View.VISIBLE not work why ?
    //@{indicator == "even" ? View.VISIBLE: View.GONE}  ==> not work
    //@{indicator.equals(`up`) ? View.VISIBLE: View.GONE} ==> work, use equals for string comparring

    private var disposable : Disposable? = null

    val myRankingBoardData: LiveData<MyRankingBoardData>
      get() = _myRankingBoardData

    init {

    }

    fun request() {
        requestMyGolfPowerPageExamResultPageInfoDetail()
    }

    private fun requestMyGolfPowerPageExamResultPageInfoDetail() {
        disposable?.let {
             if (!it.isDisposed) {
                  it.dispose()
             }
        }

        pageIndex?.let {
           disposable =   repository.getMyGolfPowerPageExamResultPageInfoDetail(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               .doOnSubscribe {
                   showLoading()
               }
               .doAfterTerminate {
                   hideLoading()
               }
                .doOnError { error ->
                      errorLog(_classNameForLog,error)
                }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        if (body.data.errorCode == 0) {
                            val myRankingBoardData  = MyRankingBoardData.from(body.data.res)
                            _myRankingBoardData.value = myRankingBoardData
                            println(myRankingBoardData.toString())
                        } else {
                            _serverError.value = CONSTANT_ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                    }
                },{ e ->
                   when(e) {
                       is NetworkErrorException -> _networkFail.value = Unit
//                       is HttpException -> {
//                            if( e.code == 419)  { _appRefresh.call()}
//                       }
                       else -> _serverError.value = CONSTANT_ERROR_MESSAGE_UNKNOWN_ERROR
                   }
                })
                .addTo(compositeDisposable)
        }
    }
}