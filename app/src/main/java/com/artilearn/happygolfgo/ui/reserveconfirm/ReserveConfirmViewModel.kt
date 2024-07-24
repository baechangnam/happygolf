package com.artilearn.happygolfgo.ui.reserveconfirm

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ApiError
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.data.reserveconfirm.*
import com.artilearn.happygolfgo.data.reserveconfirm.source.CancelRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReservationRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReserveConfirmRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.TimeChangeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ReserveConfirmViewModel(
    private val reserveConfirmRepository: ReserveConfirmRepository,
    private val reservationRepository: ReservationRepository,
    private val cancelRepository: CancelRepository,
    private val timeChangeRepository: TimeChangeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _status: MutableLiveData<ReservationStatus> = MutableLiveData()
    private val _plateModel: SingleLiveEvent<PlateResponse> = SingleLiveEvent()
    private val _lessonModel: MutableLiveData<LessonResponse> = MutableLiveData()
    private val _policy: MutableLiveData<String> = MutableLiveData()
    private val _unKnowError: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _reservationPlate: MutableLiveData<ReservationPlate> = MutableLiveData()
    private val _reservationLesson: MutableLiveData<ReservationLesson> = MutableLiveData()
    private val _buttonClickable: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _reservationTimeChange: MutableLiveData<ReservationPlate> = MutableLiveData()

    val status: LiveData<ReservationStatus>
        get() = _status

    val plateModel: LiveData<PlateResponse>
        get() = _plateModel

    val lessonModel: LiveData<LessonResponse>
        get() = _lessonModel

    val policy: LiveData<String>
        get() = _policy

    val unKnowError: LiveData<Unit>
        get() = _unKnowError

    val reservationPlate: LiveData<ReservationPlate>
        get() = _reservationPlate

    val reservationLesson: LiveData<ReservationLesson>
        get() = _reservationLesson

    val buttonClickable: LiveData<Unit>
        get() = _buttonClickable

    val reservationTimeChange: LiveData<ReservationPlate>
        get() = _reservationTimeChange

    var plateAvailabilityId: Int? = null
    var lessonAvailabilityId: Int? = null
    var ticketId: Int? = null
    var plateTimeChange: Int? = null

    var isMultipleReservation : Boolean = false //최대 일일 동시 예약수는 3회 이다.
    var firstPlateAvailabilityID: Int = 0
    var secondPlateAvailabilityID: Int = 0
    var thirdPlateAvailabilityID: Int = 0

    var firstPlateTime : String = ""
    var secondPlateTime : String = ""
    var thirdPlateTime : String = ""

    fun requestPolicy() {
        compositeDisposable.add(
            reserveConfirmRepository.requestPolicy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        _policy.value = body.data.policy.body
                    } else {
                        _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                }, {  t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = "데이터를 불러오는데 실패했습니다"
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                })
        )
    }

    //feature/2022/0216/multiSelection
    //타석 예약하는 부분
    fun reservationThreeOnTimePlate(firstPlateAvailabilityID:Int = 0, secondPlateAvailabilityId:Int=0, thirdPlateAvailabilityId : Int = 0) {
        if (ticketId != null && plateAvailabilityId != null) {
            val dataModel = ReserveThreeOneTimePlateModel(ticketId!!, firstPlateAvailabilityID ,secondPlateAvailabilityId, thirdPlateAvailabilityId)
            val data = hashMapOf<String, ReserveThreeOneTimePlateModel>()
            data["Data"] = dataModel
            compositeDisposable.add(
                reservationRepository.reservationThreeOneTimePlate(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .doOnError { t -> errorLog(TAG, t) }
                    .subscribe({ response ->
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _reservationPlate.value = body.data.plateReservation
                        } else {
                            _buttonClickable.call()
                            val erroBody = response.errorBody()
                            if (erroBody == null) {
                                _serverError.value = "오류로 인한 예약실패"
                            } else {
                                val gson = Gson()
                                val type = object : TypeToken<ApiError>() {}.type
                                val errorResponse: ApiError =
                                    gson.fromJson(erroBody.charStream(), type)
                                when (errorResponse.resultCode) {
                                    4110 -> _serverError.value = "사용 불가능한 이용권 입니다"
                                    4102 -> _serverError.value = "최대 예약횟수는 3회 입니다"
                                    4117 -> _serverError.value = "예약가능한 타석이 없습니다"
                                    4103, 4116 -> _serverError.value = "당일 예약이 가득 찼습니다"
                                    4118 -> _serverError.value = "요청 정보를 알 수 없습니다"
                                    4112 -> _serverError.value = "해당 티켓은 타석 이용권이 아닙니다"
                                }
                            }
                        }
                    }, { t ->
                        _buttonClickable.call()
                        when (t) {
                            is NetworkErrorException -> _networkFail.value = Unit
                            is HttpException -> {
                                if (t.code() == 419) _appRefresh.call()
                                else _serverError.value = "데이터를 불러오는데 실패했습니다"
                            }
                            else -> _serverError.value = "알 수 없는 오류 발생"
                        }
                    })
            )
        } else {
            _unKnowError.call()
        }
    }
    fun reservationPlate() {
        if (ticketId != null && plateAvailabilityId != null) {
            val dataModel = ReservePlateModel(ticketId!!, plateAvailabilityId!!)
            val data = hashMapOf<String, ReservePlateModel>()
            data["Data"] = dataModel
            compositeDisposable.add(
                reservationRepository.reservationPlate(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .doOnError { t -> errorLog(TAG, t) }
                    .subscribe({ response ->
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _reservationPlate.value = body.data.plateReservation
                        } else {
                            _buttonClickable.call()
                            val erroBody = response.errorBody()
                            if (erroBody == null) {
                                _serverError.value = "오류로 인한 예약실패"
                            } else {
                                val gson = Gson()
                                val type = object : TypeToken<ApiError>() {}.type
                                val errorResponse: ApiError =
                                    gson.fromJson(erroBody.charStream(), type)
                                when (errorResponse.resultCode) {
                                    4110 -> _serverError.value = "사용 불가능한 이용권 입니다"
                                    4102 -> _serverError.value = "최대 예약횟수는 3회 입니다"
                                    4117 -> _serverError.value = "예약가능한 타석이 없습니다"
                                    4103, 4116 -> _serverError.value = "당일 예약이 가득 찼습니다"
                                    4118 -> _serverError.value = "요청 정보를 알 수 없습니다"
                                    4112 -> _serverError.value = "해당 티켓은 타석 이용권이 아닙니다"
                                }
                            }
                        }
                    }, { t ->
                        _buttonClickable.call()
                        when (t) {
                            is NetworkErrorException -> _networkFail.value = Unit
                            is HttpException -> {
                                if (t.code() == 419) _appRefresh.call()
                                else _serverError.value = "데이터를 불러오는데 실패했습니다"
                            }
                            else -> _serverError.value = "알 수 없는 오류 발생"
                        }
                    })
            )
        } else {
            _unKnowError.call()
        }
    }

    fun reservationLesson() {
        if (ticketId != null && lessonAvailabilityId != null) {
            val dataModel = ReserveLessonModel(ticketId!!, lessonAvailabilityId!!)
            val data = hashMapOf<String, ReserveLessonModel>()
            data["Data"] = dataModel
            compositeDisposable.add(
                reservationRepository.reservationLesson(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .doOnError { t -> errorLog(TAG, t) }
                    .subscribe({ response ->
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _reservationLesson.value = body.data.lessonReservaion
                        } else {
                            _buttonClickable.call()
                            _serverError.value = "오류로 인한 예약실패"
                        }
                    }, { t ->
                        _buttonClickable.call()
                        when (t) {
                            is NetworkErrorException -> _networkFail.value = Unit
                            is HttpException -> {
                                if (t.code() == 419) _appRefresh.call()
                                else _serverError.value = "데이터를 불러오는데 실패했습니다"
                            }
                            else -> _serverError.value = "알 수 없는 오류 발생"
                        }
                    })
            )
        } else {
            _unKnowError.call()
        }
    }

    fun getReservationLessonDetail(lessonId: Int) {
        reserveConfirmRepository.getReservationLessonDetail(lessonId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ result ->
                val body = result.body()
                if (result.isSuccessful && body != null) {
                    _lessonModel.value = body.data
                } else {
                    _serverError.value = "예약 정보를 가지고 오지 못했습니다"
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

    fun getReservationPlateDetail(plateId: Int) {
        reserveConfirmRepository.getReservationPlateDetail(plateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ result ->
                val body = result.body()
                if (result.isSuccessful && body != null) {
                    _plateModel.value = body.data
                } else {
                    _serverError.value = "예약 정보를 가지고 오지 못했습니다"
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

    fun reservationPlateCancel(plateId: Int) {
        compositeDisposable.add(
            cancelRepository.reservationPlateCancel(plateId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({
                    _status.value = ReservationStatus.CANCEL_SUCCESS
                }, { t ->
                    _buttonClickable.call()
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = "데이터를 불러오는데 실패했습니다"
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                })
        )
    }

    fun reservationLessonCancel(lessonId: Int) {
        compositeDisposable.add(
            cancelRepository.reservationLessonCancel(lessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({
                    _status.value = ReservationStatus.CANCEL_SUCCESS
                }, { t ->
                    _buttonClickable.call()
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = "데이터를 불러오는데 실패했습니다"
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                })
        )
    }

    fun reservationPlateTimeChange() {
        val timeChangeModel = PlateTimeChangeModel(
            ticketID = ticketId ?: 0,
            plateReservationID = plateTimeChange ?: 0,
            plateAvailabilityID = plateAvailabilityId ?: 0
        )

        val data = hashMapOf<String, PlateTimeChangeModel>()
        data["Data"] = timeChangeModel

        timeChangeRepository.timeChnage(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ item ->
                val plate = ReservationPlate(
                    id = item.id,
                    name = item.name,
                    status = item.status,
                    startDate = item.startDate,
                    endDate = item.endDate,
                    plateNumber = item.plateNumber,
                    remainingCount = item.remainingCount,
                    totalCount = item.totalCount,
                    isUnlimited = item.isUnlimited,
                    secondStartDate = "0",
                    secondEndDate = "0",
                    thirdStartDate = "0",
                    thirdEndDate = "0",
                )
                _reservationTimeChange.value = plate
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).let(compositeDisposable::add)
    }

    enum class ReservationStatus {
        CANCEL_SUCCESS,
        CANCEL_FAIL
    }

}