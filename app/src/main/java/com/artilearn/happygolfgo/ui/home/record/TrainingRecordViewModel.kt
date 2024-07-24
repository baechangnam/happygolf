package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.banner.Banner
import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.exam.Exam
import com.artilearn.happygolfgo.data.exam.ExamDto
import com.artilearn.happygolfgo.data.exam.YearMonth
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPowerExamResultDto
import com.artilearn.happygolfgo.data.exam.state.request.ExamStateBody
import com.artilearn.happygolfgo.data.gamecenter.TrainingManagementSummaryGolfPowerResponse
import com.artilearn.happygolfgo.data.gamecenter.TrainingManagementSummaryRoundResponse
import com.artilearn.happygolfgo.data.gamecenter.TrainingManagementSummaryTimeResponse
import com.artilearn.happygolfgo.data.gamecenter.TrainingManagementSummaryWeightResponse
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.ui.home.record.mapper.*
import com.artilearn.happygolfgo.ui.home.record.model.*
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TrainingRecordViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {

    private val _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE = "데이타를 불러오는데 실패했습니다."
    private val _ERROR_MESSAGE_UNKNOWN_ERROR  = "알 수 없는 오류 발생"
    private val TAG = javaClass.simpleName
    private val _summaryWeight: MutableLiveData<TRSummaryWeightModel> = MutableLiveData()
    private val _summaryGoldPower: MutableLiveData<TRSummaryGolfPowerModel> = MutableLiveData()
    private val _golfPowerRecords: MutableLiveData<List<TRSummaryGolfPowerRecordModel>> = MutableLiveData()
    private val _summaryRound: MutableLiveData<TRSummaryRoundModel> = MutableLiveData()
    private val _roundRecords: MutableLiveData<List<TRSummaryRoundRecordModel>> = MutableLiveData()
    private val _adapterPageInfo: MutableLiveData<TRMyGolfPowerExamResultPageInfoModel> = MutableLiveData()

    private var yearMonth = ""
    var examYearMonth = ""
    var examHistory = listOf<YearMonth>()

    private val _liveBanners: MutableLiveData<List<Banner>> = MutableLiveData()
    val liveBanner: LiveData<List<Banner>> = _liveBanners

    private val _liveMonth: MutableLiveData<Int> = MutableLiveData()
    val liveMonth: LiveData<Int> = _liveMonth

    private val _liveExamState: MutableLiveData<Exam> = MutableLiveData()
    val liveExamState: LiveData<Exam> = _liveExamState

    private val _liveGolfPower: MutableLiveData<com.artilearn.happygolfgo.data.exam.golfpower.Res> = MutableLiveData()
    val liveGolfPower: LiveData<com.artilearn.happygolfgo.data.exam.golfpower.Res> = _liveGolfPower

    private val _liveState: MutableLiveData<Exam.ExamState> = MutableLiveData()
    val liveState: LiveData<Exam.ExamState> = _liveState

    // 2023.04.05 summary time removed
    private val _summaryTime: MutableLiveData<TRSummaryTimeModel> = MutableLiveData()
    val summaryTime: LiveData<TRSummaryTimeModel>
        get() = _summaryTime

    val adapterPageInfo: LiveData<TRMyGolfPowerExamResultPageInfoModel>
        get() =  _adapterPageInfo

    val summaryWeight: LiveData<TRSummaryWeightModel>
        get() = _summaryWeight

    val summaryGolfPower: LiveData<TRSummaryGolfPowerModel>
        get() = _summaryGoldPower

    val golfPowerRecords:LiveData<List<TRSummaryGolfPowerRecordModel>>
        get() = _golfPowerRecords

    val summaryRound:LiveData<TRSummaryRoundModel>
        get() = _summaryRound

    init {
        requestSummaryTime()
        requestSummaryWeight()
        requestSummaryGoldPower()
        requestSummaryRound()
        requestTranningBanners()
        Handler().postDelayed({
            getExamState()
            getMyGolfPowerPageExamResultPageInfoDetail()
        },100)

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyyMM")
        val formattedDate = formatter.format(calendar.time)
        setYearMonth(formattedDate)
    }

    fun setYearMonth(yearMonth: String) {
        this.yearMonth = yearMonth
        _liveMonth.value = extractMonth(yearMonth)
    }

    fun getYearMonth(): String {
        return yearMonth
    }

    private fun extractMonth(dateStr: String): Int? {
        return if (dateStr.length == 6) {
            dateStr.substring(4, 6).toIntOrNull()
        } else {
            null
        }
    }

    data class CombinedResult(
        val summaryTime: TrainingManagementSummaryTimeResponse,
        val summaryWeight: TrainingManagementSummaryWeightResponse,
        val summaryGolfPower: TrainingManagementSummaryGolfPowerResponse,
        val summaryRound: TrainingManagementSummaryRoundResponse,
        val tranningBanners: BannerDto,
        val examState: ExamDto,
        val golfPowerPageExamResult: GolfPowerExamResultDto
    )

//    2023.05.02 시험 정보에 대한 정보 rollback
    private fun requestMyGolfPowerExamResultPageInfo() {
        repository.getMyGolfPowerExamResultPageInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  t -> errorLog(TAG,t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _adapterPageInfo.value = TRMyGolfPowerExamResultPageInfoModelMapper.mapper(body.data.res)
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }
                ,{ t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                        else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                    }
                }).addTo(compositeDisposable)
    }

    private fun requestSummaryTime() {
        repository.getTrainingManagementSummaryTime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryTime.value = TRSummaryTimeMapper.mapper(body.data.res);
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                    else -> _serverError.value =  _ERROR_MESSAGE_UNKNOWN_ERROR
                }
            }).addTo(compositeDisposable)
    }

    private fun requestSummaryWeight() {
        repository.getTrainingManagementSummaryWeight()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { showLoading() }
//            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode <= 0 ) {
                        _summaryWeight.value = TRSummaryWeightMapper.mapper(body.data.res);
                    } else {
                        if (body.data.errorMessage.isNotEmpty()) {
                            _serverError.value = body.data.errorMessage
                        }
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                    else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                }
            }).addTo(compositeDisposable)
    }

    private fun requestSummaryGoldPower() {
        repository.getTrainingManagementSummaryGolfPower()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode < 0) {
                        _summaryGoldPower.value = TRSummaryGolfPowerMapper.mapper(body.data.res)
                        _golfPowerRecords.value = TrainingManagementGolfPowerRecordsMapper.mapper(body.data.res)
                    } else {
                        if (body.data.errorMessage.isNotEmpty()) {
                            _serverError.value = body.data.errorMessage
                        }
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                    else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                }
            }).addTo(compositeDisposable)
    }

    private fun requestSummaryRound() {
        repository.getTrainingManagementSummaryRound()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryRound.value = TRSummaryRoundMapper.mapper(body.data.res)
//                        _golfPowerRecords.value = TrainingManagementGolfPowerRecordsMapper.mapper(body.data.res);
                    } else {
                        _serverError.value =  _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                    else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                }
            }).addTo(compositeDisposable)
    }

    private fun requestTranningBanners() {
        repository.getTraningBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  t -> errorLog(TAG,t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _liveBanners.value = body.data.res.banners
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }
                ,{ t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                        else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                    }
                }).addTo(compositeDisposable)
    }

    private fun getExamState() {
        repository.getExamState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  t -> errorLog(TAG,t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _liveExamState.value = body.data.res.exam
                        examYearMonth = body.data.res.exam.currentYearMonth
                        examHistory = body.data.res.yearMonths
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }
                ,{ t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                        else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                    }
                }).addTo(compositeDisposable)
    }

    fun postJoinMonthlyExamination() {
        val hashMap = hashMapOf<String, ExamStateBody>()
        hashMap["Data"] = ExamStateBody(examYearMonth)
        repository.postJoinMonthlyExamination(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  t -> errorLog(TAG,t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
//                        getExamState()
                        _liveState.value = body.data.res.state
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }
                ,{ t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                        else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                    }
                }).addTo(compositeDisposable)
    }

    fun getMyGolfPowerPageExamResultPageInfoDetail() {
        repository.getMyGolfPowerPageExamResultPageInfoDetail(yearMonth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  t -> errorLog(TAG,t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _liveGolfPower.value = body.data.res
                    } else {
                        _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                } else {
                    _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                }
            }
                ,{ t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = _ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                        }
                        else -> _serverError.value = _ERROR_MESSAGE_UNKNOWN_ERROR
                    }
                }).addTo(compositeDisposable)
    }
}