package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.profilechange.source.ProfileChangeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.asMultipart
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class ProfileChangeViewModel(
    private val repository: ProfileChangeRepository,
    private val applicationContext: Context
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _gallerySubject: Subject<Unit> = PublishSubject.create()
    private val _defaultImageSubject: Subject<Unit> = PublishSubject.create()
    private val _buttonClickSubject: Subject<Unit> = PublishSubject.create()
    private val _buttonTypeSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val _buttonTextResult: BehaviorSubject<String> = BehaviorSubject.createDefault("취소")
    private val _urlSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    private val _profileImage: SingleLiveEvent<String?> = SingleLiveEvent()
    val profileImage: LiveData<String?>
        get() = _profileImage

    private val _state: SingleLiveEvent<ProfileChangeState> = SingleLiveEvent()
    val state: LiveData<ProfileChangeState>
        get() = _state

    private val _buttonText: MutableLiveData<String> = MutableLiveData()
    val buttonText: LiveData<String>
        get() = _buttonText

    init {
        _profileImage.value = repository.profileImage

        compositeDisposable.addAll(
            _gallerySubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setState(ProfileChangeState.GALLERY)
                    _buttonTypeSubject.onNext(true)
                },
            _defaultImageSubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setState(ProfileChangeState.DEFAULT_IMAGE)
                    _buttonTypeSubject.onNext(false)
                },
            _buttonClickSubject.throttleFirst(1, TimeUnit.SECONDS)
                .withLatestFrom(_buttonTextResult, BiFunction { _: Unit, x: String -> x })
                .subscribe {
                    if (it == "변경") {
                        checkUriState()
                    } else {
                        setState(ProfileChangeState.BACK)
                    }
                },
            _buttonTextResult.observeOn(AndroidSchedulers.mainThread())
                .subscribe(_buttonText::setValue)
        )
    }

    fun onGalleryClick() = _gallerySubject.onNext(Unit)

    fun onDefaultImageClick() = _defaultImageSubject.onNext(Unit)

    fun onButtonClick() = _buttonClickSubject.onNext(Unit)

    fun setProfileImage(image: String?) {
        _profileImage.value = image
    }

    fun onNextUri(uri: String) = _urlSubject.onNext(uri)

    fun onNextButtonText(text: String) = _buttonTextResult.onNext(text)

    private fun checkUriState() {
        if (_urlSubject.value.isNullOrEmpty()) {
            uploadDefaultProfileImage()
        } else {
            uploadProfileImage(_urlSubject.value!!.toUri().asMultipart("file", applicationContext.contentResolver))
        }
    }

    private fun uploadProfileImage(body: MultipartBody.Part?) {
        body?.let {
            repository.uploadProfileImage(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    setState(ProfileChangeState.SUCCESS)
                }, { t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.call()
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = "사진 업로드에 실패했습니다\n${t.code()}"
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                }).addTo(compositeDisposable)
        }
    }

    private fun uploadDefaultProfileImage() {
        repository.uploadDefaultProfileImage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({
                setState(ProfileChangeState.SUCCESS)
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "사진 업로드에 실패했습니다\n${t.code()}"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun setState(state: ProfileChangeState) = _state.postValue(state)

    enum class ProfileChangeState {
        BACK,
        SUCCESS,
        GALLERY,
        DEFAULT_IMAGE
    }
}