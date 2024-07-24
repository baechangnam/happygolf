package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.record.mapper.TrainingManagementSwingVideoRecordsMapper
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TrainingRecordSwingVideoListViewModel(
     private val repository: GameCenterRepository
) : BaseViewModel() {
     private val TAG = javaClass.simpleName
     private val _emptyVideoList: MutableLiveData<Boolean> = MutableLiveData()
     private val _swingVideos: MutableLiveData<ArrayList<TRSummarySwingVideoRecordModel>> = MutableLiveData()
     val emptyVideoList: LiveData<Boolean>
          get() = _emptyVideoList
     val swingVideos: LiveData<ArrayList<TRSummarySwingVideoRecordModel>>
          get() = _swingVideos

     init {
          requestSummarySwingVideo()
     }

     public fun refreshSwingList() {
          requestSummarySwingVideo()
     }

     private fun requestSummarySwingVideo() {
          repository.getTrainingManagementSummarySwingVideo()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .doOnSubscribe { showLoading() }
               .doAfterTerminate { hideLoading() }
               .doOnError { t -> errorLog(TAG, t) }
               .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                         if (body.data.errorCode == 0) {
                              if(body.data.res.records.isNotEmpty()) {
                                    hideEmptySwingVideos()
                                   _swingVideos.value =
                                        (TrainingManagementSwingVideoRecordsMapper.mapper(body.data.res)) as ArrayList<TRSummarySwingVideoRecordModel>
                              } else {
                                   showEmptySwingVideos();
                              }
                         } else {
                              _serverError.value = "데이타를 불러오는데 실패했습니다."
                         }
                    } else {
                         _serverError.value = "데이타를 불러오는데 실패했습니다."
                    }
               }, { t ->
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


     private fun showEmptySwingVideos() {
          _emptyVideoList.value = true
     }

     private fun hideEmptySwingVideos() {
          _emptyVideoList.value = false
     }

}