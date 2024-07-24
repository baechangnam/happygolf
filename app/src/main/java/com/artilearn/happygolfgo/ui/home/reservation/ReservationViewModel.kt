package com.artilearn.happygolfgo.ui.home.reservation

import android.accounts.NetworkErrorException
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.banner.Announcement
import com.artilearn.happygolfgo.data.banner.Banner
import com.artilearn.happygolfgo.data.examination.Examination
import com.artilearn.happygolfgo.data.home.HomeResponseInfo
import com.artilearn.happygolfgo.data.home.JoinMonthlyExamInputModel
import com.artilearn.happygolfgo.data.home.RefreshFcmTokenPutParam
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.data.version.MobileVersionAndroid
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.EXPIRED_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.PAUSE_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.USE_TICKET
import com.artilearn.happygolfgo.util.PreferenceManager
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

//comments: Eddie Kim, 홈화면에서 티켓에 대한 정보를 가져오는 부분
class ReservationViewModel(
    private val repository: HomeRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val reservationList: Subject<Unit> = PublishSubject.create()
    private val _isRefreshSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val _onRefreshSubject: PublishSubject<Unit> = PublishSubject.create()
    private var disposable: Disposable? = null
    private var disposableForAnnouncmentAndExam : Disposable? = null
    private var disposableForJoinMonthlyExam : Disposable? = null
    private var disposableForRefreshFcmToken : Disposable? = null
    private var disposableGetBanner : Disposable? = null

    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
    private val _reservationListButton: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _message = SingleLiveEvent<String>()
    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _isRefresh: MutableLiveData<Boolean> = MutableLiveData()

    private val _liveBanners: MutableLiveData<List<Banner>> = MutableLiveData()
    val liveBanner: LiveData<List<Banner>> = _liveBanners

    val isRefresh: LiveData<Boolean>
        get() = _isRefresh

    val emptyTicket: LiveData<Boolean>
        get() = _emptyTicket

    val reservationListButton: LiveData<Unit>
        get() = _reservationListButton

    val message: LiveData<String>
        get() = _message

    val buttonState: LiveData<Boolean>
        get() = _buttonState

    private val _ticketList: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
    val ticketList: LiveData<ArrayList<Ticket>>
        get() = _ticketList

    private val _retryExamPermission : SingleLiveEvent<Unit> = SingleLiveEvent()
    val retryExamPermission: LiveData<Unit>
     get() = _retryExamPermission

    private val _announcement: MutableLiveData<Announcement> = MutableLiveData()
    val announcement:LiveData<Announcement>
        get() = _announcement

    private val _showAnnouncement = MutableLiveData(false)
    val showAnnouncement: LiveData<Boolean> = _showAnnouncement

    private val _examination:MutableLiveData<Examination> = MutableLiveData()
    val examination:LiveData<Examination>
       get() = _examination

    private val _mobileVersionAndroid:MutableLiveData<MobileVersionAndroid> = MutableLiveData()
    val mobileVersionAndroid:LiveData<MobileVersionAndroid>
        get() = _mobileVersionAndroid

    private val _announcementClick = SingleLiveEvent<String>()
    val announcementClick:LiveData<String>
        get() = _announcementClick

    private val _scoreClick = SingleLiveEvent<String>()
    val scoreClick:LiveData<String>
        get() = _scoreClick

    private val _todayReservations = SingleLiveEvent<List<String>>()
    val todayReservation:LiveData<List<String>>
        get() = _todayReservations

    init {
        compositeDisposable.addAll(
            reservationList.throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _reservationListButton.call() },

            _onRefreshSubject.observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _isRefreshSubject.onNext(true) }
                .map { _ticketList.value?.size }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _isRefreshSubject.onNext(false) }
                .subscribe { it?.let {
                    fetchData()
                } },

            _isRefreshSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(_isRefresh::setValue)
        )

        checkFcmToken();
//        registerFcmToken()
//        checkFcmToken();
    }

    fun onReservationList() {
        reservationList.onNext(Unit)
    }

    fun onClickAnnouncement() {
        announcement.value?.let {
            _announcementClick.value = it.serviceUrl
        }
    }

    fun onClickTrainingCenter() {
        _scoreClick.value = "go to Training"
    }

    //TODO: bounce or throttle
    fun onClickTakeExam() {
       Log.d("log", "onClickTakeExam")
        if (examination.value?.state == "taken") {
            _retryExamPermission.call()
        } else {
            requestJoinMonthlyExam()
        }
    }

    private fun checkFcmToken() {
        val lastUpdatedAt =  preferenceManager.fcmTokenUpdatedDate
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val current = formatter.format(date)
        if  (lastUpdatedAt != current) {
            preferenceManager.fcmTokenUpdatedDate = current
            registerFcmToken()
        }
    }

    private fun registerFcmToken() {
         FirebaseMessaging.getInstance().token
             .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && !TextUtils.isEmpty(task.result)) {
                     val token: String = task.result !!
                     putFcmToken(token)
                }
             }
    }

    //POST METHOD CALLING
    fun requestJoinMonthlyExam() {
        disposableForJoinMonthlyExam?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        val dataModel = JoinMonthlyExamInputModel(
            examination.value?.golfPowerExamCategoryID ?: "-1",
            examination.value?.golfPowerMonthlyExamID ?: "-1",
            examination.value?.state ?: "waiting"
            )
        //POST Method
        val data = hashMapOf<String, JoinMonthlyExamInputModel>()
        data["Data"] = dataModel

        disposableForJoinMonthlyExam = repository.requestJoinMonthlyExam(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ it ->
                val body = it.body()
                if (it.isSuccessful) {
                    if (body  != null && body.data.errorCode == 0 ) {
                        _examination.value?.let { v ->
                            val  exam = v.copy()
                            exam.state = body.data.res.state
                            exam.golfPowerMonthlyExamID  = body.data.res.golfPowerMonthlyExamID
                            setExaminiationComputedValue(exam)
                            resetExamScore(exam)
                            _examination.value = exam
                        }
                    } else {
                        body?.data?.errorMessage.let {
                            _serverError.value = "오률 메세지: $it"
                        }
                    }
                } else {
                    _serverError.value = "재시험 요청에 실패하였습니다. 다시 시도해 주십시요."
                }
            }, { t ->
                when (t) {
//                    is NetworkErrorException -> _networkFail.call()
//                    is HttpException -> {
//                        if (t.code() == 419) _appRefresh.call()
//                        else _serverError.value = "이용권을 불러오는데 실패했습니다"
//                    }
//                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun resetExamScore(exam: Examination) {
        with(exam) {
            putting = "-"
            sg = "-"
            irn = "-"
            wu = "-"
            drv = "-"
        }
    }
    private fun setExaminiationComputedValue(exam:Examination) {
        with(exam) {
            isWaiting = false
            actionButtonOn = false
            if (state == "waiting") {
                isWaiting = true
                stateNumber = -1
                actionTitle = ""
            } else if (state == "finished") {
                stateNumber = 0
                actionTitle = "응시완료"
            } else if (state == "take") {
                stateNumber = -2
                actionTitle = "응시하기"
                actionButtonOn = true
            } else if (state == "taking") {
                stateNumber = 1
                actionTitle = "응시중"
            } else if (state == "taken") {
                stateNumber = 2
                actionTitle = "재시험"
                actionButtonOn = true
            } else if (state == "takingAgain") {
                stateNumber = 3
                actionTitle = "응시중"
            } else if (state == "takenAgain") {
                stateNumber = 4
                actionTitle = "응시완료"
            } else if (state == "uncompleted") {
                stateNumber = 5
                actionTitle = "응시완료"
            }
        }
        return
    }

    private fun getAnnouncementAndExam() {
        disposableForAnnouncmentAndExam?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        disposableForAnnouncmentAndExam = repository.requestAnnouncementAndExam()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful) {
                    if (body  != null && body.data.errorCode == 0 ) {
                        setExaminiationComputedValue(body.data.res.exam)
//                        _announcement.value =  body.data.res.announcement
                        _examination.value = body.data.res.exam
                        _mobileVersionAndroid.value = body.data.res.version.androidVersion
                        _showAnnouncement.value = !body.data.res.announcement.subject.isNullOrBlank()
                    } else {
                        body?.data?.errorMessage.let { msg ->
                            _serverError.value = "오류 메세지: $msg"
                        }
                        _showAnnouncement.value = false
                    }
                } else {
                    _serverError.value = "시험 정보를 받아오는데 실패하였습니다."
                    _showAnnouncement.value = false
                }
            }, { t ->
                when (t) {
//                    is NetworkErrorException -> _networkFail.call()
//                    is HttpException -> {
//                        if (t.code() == 419) _appRefresh.call()
//                        else _serverError.value = "이용권을 불러오는데 실패했습니다"
//                    }
//                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun getBanner() {
        disposableGetBanner?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        disposableGetBanner = repository.getBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful) {
                    if (body != null && body.data.errorCode == 0 ) {
                        _liveBanners.value = body.data.res.banners
                        _announcement.value =  body.data.res.announcement
                    } else {
                        body?.data?.errorMessage.let { msg ->
                            _serverError.value = "오류 메세지: $msg"
                        }
                        _showAnnouncement.value = false
                    }
                } else {
                    _serverError.value = "배너 정보를 받아오는데 실패하였습니다."
                    _showAnnouncement.value = false
                }
            }, { t ->
            }).addTo(compositeDisposable)
    }

    fun fetchData() {
        getTicket()
        getAnnouncementAndExam()
        getTodaysReservation()
        getBanner()
    }

    fun putFcmToken(fcmToken:String) {
        disposableForRefreshFcmToken?.let {
             if(!it.isDisposed) {
                 it.dispose()
             }
        }

        val dataModel = RefreshFcmTokenPutParam(
            "1", // 1 for android
            fcmToken //val fcmToken:String
        )

        //POST Method
        val data = hashMapOf<String, RefreshFcmTokenPutParam>()
        data["Data"] = dataModel

        disposableForRefreshFcmToken = repository.requestFreshFcmToken(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { showLoading() }
//            .doAfterTerminate { hideLoading() }
//            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ it ->
                val body = it.body()
            }, { t ->
                when (t) {
//                    is NetworkErrorException -> _networkFail.call()
//                    is HttpException -> {
//                        if (t.code() == 419) _appRefresh.call()
//                        else _serverError.value = "이용권을 불러오는데 실패했습니다"
//                    }
//                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    fun getTicket() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        disposable = repository.requestHomeTicket()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful) {
                    if (body != null && body.data.tickets.isNotEmpty()) {
                        hideEmptyTicket()
                        _ticketList.value = allTicketList(body.data)
                    } else {
                        showEmptyTicket()
                    }
                } else {
                    _serverError.value = "서버로부터 이용권을 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "이용권을 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    fun getTodaysReservation() {
        repository.selectReservationList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
                val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                if (response.isSuccessful && body != null) {
                    val plateItems = body.data.plateReservations.filter {
                        val date = timeFormat.parse(it.startDate)
                        val dateStr = SimpleDateFormat("yyyy-MM-dd").format(date)
                        dateStr == today
                    }.map {
                        val date = timeFormat.parse(it.startDate)
                        val time = SimpleDateFormat("HH:mm").format(date)
                        "${it.name} $time"
                    }
                    _todayReservations.value = plateItems
                } else {
                    _serverError.value = "예약 정보를 불러오는데 실패했습니다"
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

    private fun showEmptyTicket() {
        _emptyTicket.value = true
    }

    private fun hideEmptyTicket() {
        _emptyTicket.value = false
    }

    fun onRefresh() = _onRefreshSubject.onNext(Unit)

    fun onButtonState(result: Boolean) {
        _buttonState.value = result
    }

    private fun allTicketList(item: HomeResponseInfo): ArrayList<Ticket> {
        val newTicketList = arrayListOf<Ticket>()

        val useList = mapperUseTicket(item.tickets)
        val pauseList = mapperPauseTicket(item.pausedTickets)
        val expiredList = mapperExpiredTicket(item.expiredTickets)

        if (useList.isNotEmpty()) {
            for (i in useList.indices) {
                newTicketList.add(useList[i])
            }
        }

        if (pauseList.isNotEmpty()) {
            for (i in pauseList.indices) {
                newTicketList.add(pauseList[i])
            }
        }

        if (expiredList.isNotEmpty()) {
            for (i in expiredList.indices) {
                newTicketList.add(expiredList[i])
            }
        }

        return newTicketList
    }

    private fun mapperUseTicket(item: List<Ticket>): List<Ticket> {
        return item.map {
            Ticket(
                viewType = USE_TICKET,
                id = it.id,
                name = it.name,
                type = it.type,
                period = it.period,
                totalCount = it.totalCount,
                usedCount = it.usedCount,
                onlyOnceToday = it.onlyOnceToday,
                remainingCount = it.remainingCount,
                isUnlimited = it.isUnlimited,
                startDate = it.startDate,
                endDate = it.endDate,
                allowToBook = it.allowToBook,
                ignoreLimit = it.ignoreLimit, //DONETODO: flag to ignore max reservation limit to see opened reseration time schedule
                multipleReservation = it.multipleReservation,//DONETODO flag to select multiple time in a reservation for songdo branch
                maxMultipleCount = it.maxMultipleCount,//DONETODO count for max reservation selection in a reservation for songdo branc
                branchName = it.branchName, //doneTODO:
                multipleBranchTicketUser = it.multipleBranchTicketUser, //doneTODO:
                pasuePeriods = it.pasuePeriods,
                totalPrice = it.totalPrice,
                maxReservationCount = it.maxReservationCount
            )
        }
    }

    private fun mapperPauseTicket(item: List<Ticket>): List<Ticket> {
        return item.map {
            Ticket(
                viewType = PAUSE_TICKET,
                id = it.id,
                name = it.name,
                type = it.type,
                period = it.period,
                totalCount = it.totalCount,
                usedCount = it.usedCount,
                onlyOnceToday = it.onlyOnceToday,
                remainingCount = it.remainingCount,
                isUnlimited = it.isUnlimited,
                startDate = it.startDate,
                endDate = it.endDate,
                allowToBook = it.allowToBook,
                ignoreLimit = it.ignoreLimit, //DONETODO: flag to ignore max reservation limit to see opened reseration time schedule
                multipleReservation = it.multipleReservation,//DONETODO flag to select multiple time in a reservation for songdo branch
                maxMultipleCount = it.maxMultipleCount,//DONETODO count for max reservation selection in a reservation for songdo branch
                branchName = it.branchName, //doneTODO:
                multipleBranchTicketUser = it.multipleBranchTicketUser, //doneTODO:
                pasuePeriods = it.pasuePeriods,
                totalPrice = it.totalPrice
            )
        }
    }

    private fun mapperExpiredTicket(item: List<Ticket>): List<Ticket> {
        return item.map {
            Ticket(
                viewType = EXPIRED_TICKET,
                id = it.id,
                name = it.name,
                type = it.type,
                period = it.period,
                totalCount = it.totalCount,
                usedCount = it.usedCount,
                onlyOnceToday = it.onlyOnceToday,
                remainingCount = it.remainingCount,
                isUnlimited = it.isUnlimited,
                startDate = it.startDate,
                endDate = it.endDate,
                allowToBook = it.allowToBook,
                ignoreLimit = it.ignoreLimit, //DONETODO: flag to ignore max reservation limit to see opened reseration time schedule
                multipleReservation = it.multipleReservation,//DONETODO flag to select multiple time in a reservation for songdo branch
                maxMultipleCount = it.maxMultipleCount,//DONETODO count for max reservation selection in a reservation for songdo branch
                branchName = it.branchName, //doneTODO:
                multipleBranchTicketUser = it.multipleBranchTicketUser, //doneTODO:
                pasuePeriods = it.pasuePeriods,
                totalPrice = it.totalPrice
            )
        }
    }
}