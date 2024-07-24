package com.artilearn.happygolfgo.ui.home.mypage

import android.accounts.NetworkErrorException
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.ui.home.mypage.mapper.MyPageMapper
import com.artilearn.happygolfgo.ui.home.mypage.model.MyPage
import com.artilearn.happygolfgo.util.Event
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.artilearn.happygolfgo.util.getWidevineID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class MyPageViewModel(
    private val repository: VersionRepository,
    private val koinContext: Context
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _appUpdate: Subject<Unit> = PublishSubject.create()
    private val _profileClickSubject: Subject<Unit> = PublishSubject.create()
    private val _defaultImageSubject: Subject<Unit> = PublishSubject.create()
    private val _bottomFragmentCancelSubject: Subject<Unit> = PublishSubject.create()
    private val _gallerySubject: Subject<Unit> = PublishSubject.create()

    private val _bottomState: MutableLiveData<Event<BottomFragmentState>> = MutableLiveData()
    val bottomState: LiveData<Event<BottomFragmentState>>
        get() = _bottomState

    private val _myPageData: SingleLiveEvent<MyPage> = SingleLiveEvent()
    val myPageData: LiveData<MyPage>
        get() = _myPageData

    private val _eventType: SingleLiveEvent<MyPageStateType> = SingleLiveEvent()
    val eventType: LiveData<MyPageStateType>
        get() = _eventType

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String>
        get() = _message

    private val _appVersion = SingleLiveEvent<String>()
    val appVersion: LiveData<String>
        get() = _appVersion

    private val mapper: MyPageMapper = MyPageMapper
    private var packageInfo: PackageInfo? = null

    init {
        getAppVersion()

        compositeDisposable.addAll(
            _appUpdate.throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _eventType.value = MyPageStateType.PLAY_STORE },

            //DONETODO 프로필 변경 막고 배포 : Don't change the profile image in user app
            //_profileClickSubject.subscribe { _eventType.value = MyPageStateType.PERMISSION },
            _defaultImageSubject.subscribe { _bottomState.value = Event(BottomFragmentState.DEFAULT_IMAGE) },
            _gallerySubject.subscribe { _bottomState.value = Event(BottomFragmentState.GALLERY) },
            _bottomFragmentCancelSubject.subscribe { _bottomState.value = Event(BottomFragmentState.CANCEL) }
        )
    }

    fun loadMyInfo() {
        _myPageData.value = mapper.myPageMapper(repository.getMypageData())
    }

    fun appUpdateOnNext() {
        _appUpdate.onNext(Unit)
    }

    fun logout() {
        val logoutModel = LogoutModel(identifier = getWidevineID())
        val data = hashMapOf<String, LogoutModel>()
        data["Data"] = logoutModel

        repository.userLogout(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _eventType.value = MyPageStateType.LOGOUT
            }, { e ->
                when (e) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (e.code() == 419) _appRefresh.call()
                        else _message.value = "로그아웃 불가"
                    }
                    else -> _message.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    fun onProfileClick() = _profileClickSubject.onNext(Unit)
    fun onGalleryClick() = _gallerySubject.onNext(Unit)
    fun onDefaultImageClick() = _defaultImageSubject.onNext(Unit)
    fun onBottomFragmentCancel() = _bottomFragmentCancelSubject.onNext(Unit)

    private fun getAppVersion() {
        repository.getAppVersion()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                setVersion()
                compareAppVersion(it.data.latestVersion)
            }, {
                setVersion()
            }).addTo(compositeDisposable)
    }

    private fun setVersion() {
        val version = loadCurrentAppVersion() ?: "0.0.0"
        _appVersion.value = version
    }

    private fun compareAppVersion(version: String?) {
        version?.let {
            val server = it.replace(".", "").toInt()
            val local = loadCurrentAppVersion()?.replace(".", "")?.toInt()

            if (local == null) {
                _eventType.value = MyPageStateType.NECESSARY_UPDATE
            } else {
                when {
                    local < server -> _eventType.value = MyPageStateType.NEED_UPDATE
                    local >= server -> _eventType.value = MyPageStateType.NECESSARY_UPDATE
                    else -> _eventType.value = MyPageStateType.NECESSARY_UPDATE
                }
            }
        }
    }

    private fun loadCurrentAppVersion(): String? {
        try {
            packageInfo = koinContext.packageManager
                .getPackageInfo(koinContext.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return packageInfo?.versionName
    }

    fun test() {
        _bottomState.value = Event(BottomFragmentState.DEFAULT_IMAGE)
    }

    enum class MyPageStateType {
        NECESSARY_UPDATE,
        NEED_UPDATE,
        LOGOUT,
        PLAY_STORE,
        PERMISSION
    }

    enum class BottomFragmentState {
        GALLERY,
        DEFAULT_IMAGE,
        CANCEL
    }

}