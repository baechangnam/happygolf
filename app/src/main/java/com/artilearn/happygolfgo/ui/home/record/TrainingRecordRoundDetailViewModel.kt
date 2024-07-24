package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.data.log.source.LogRepository
import com.artilearn.happygolfgo.ui.home.record.mapper.TrainingManagementRoundDetailMapper
import com.artilearn.happygolfgo.ui.home.record.model.TRSummaryRoundDetailModel
import com.artilearn.happygolfgo.ui.home.record.model.TRSummaryRoundDetailRecordModel
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TrainingRecordRoundDetailViewModel(
private val repository: GameCenterRepository
) : BaseViewModel()
{
    private val TAG = javaClass.simpleName

    private val _roundDetail: SingleLiveEvent<TRSummaryRoundDetailModel> = SingleLiveEvent()

    private val _roundRecords: MutableLiveData<ArrayList<TRSummaryRoundDetailRecordModel>> = MutableLiveData()
    val roundRecords: LiveData<ArrayList<TRSummaryRoundDetailRecordModel>>
        get() = _roundRecords


    val roundDetail: LiveData<TRSummaryRoundDetailModel>
       get() = _roundDetail;

    fun requestRoundDetail(gmID: Int) {
        if (gmID > 0 ) {
             requestRoundDetailByID(gmID)
        }
    }

    private fun requestRoundDetailByID(gmID: Int)  {
        repository.getTrainingManagementSummaryRoundDetail(gmID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _roundDetail.value = TrainingManagementRoundDetailMapper.mapper(body.data.res)
                    }
                    //_logDetail.value = body.data.lessonLog
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    public fun  deltaScore(index:Int) : String {
            return "buddie"
    }
}