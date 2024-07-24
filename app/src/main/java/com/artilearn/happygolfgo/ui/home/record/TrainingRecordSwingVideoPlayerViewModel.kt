package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ApiError
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.data.log.source.LogRepository
import com.artilearn.happygolfgo.data.reserveconfirm.ReserveThreeOneTimePlateModel
import com.artilearn.happygolfgo.ui.home.record.mapper.TRSummaryWeightMapper
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TrainingRecordSwingVideoPlayerViewModel(
    private val repository : GameCenterRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _videoUrl: SingleLiveEvent<String> = SingleLiveEvent()
    private val _playVideo: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _deleteVideoUrl:SingleLiveEvent<Boolean> = SingleLiveEvent()

    val playVideo: LiveData<Boolean>
        get() = _playVideo

    val videoUrl: LiveData<String>
        get() = _videoUrl

    val deleteVideoUrl: LiveData<Boolean>
        get() = _deleteVideoUrl

    fun requestVideoUrl(videoUrl: String?) {
        videoUrl?.let { id ->
            _videoUrl.value = id
        }
    }
//
    fun onVideoClick() {
        _playVideo.value = true;
    }

    fun requestUpdateSwingVideoType(id:Int, type:Int) {
        val dataModel = TSSummarySwingVideoDetailUpdateReqModel(id, type);
        val data = hashMapOf<String, TSSummarySwingVideoDetailUpdateReqModel>();
        data["Data"] = dataModel;
        repository.updateTrainingManagementSummarySwingVideoDetail( data )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                       // _summaryWeight.value = TRSummaryWeightMapper.mapper(body.data.res);
                        _deleteVideoUrl.value = true;
                    } else {
                        _deleteVideoUrl.value = false;
                        _serverError.value =  body.data.errorMessage;
                    }
                } else {
                    _serverError.value = "데이타를 불러오는데 실패했습니다."
                    _deleteVideoUrl.value = false;
                }
            }, { t ->
                _deleteVideoUrl.value = false;
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }
}