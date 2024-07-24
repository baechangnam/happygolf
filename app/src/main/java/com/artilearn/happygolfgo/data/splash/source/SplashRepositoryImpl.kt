package com.artilearn.happygolfgo.data.splash.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.splash.*
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import com.artilearn.happygolfgo.util.PreferenceManager
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class SplashRepositoryImpl(
    private val splashLocalDataSource: SplashLocalDataSource,
    private val splashRemoteDataSource: SplashRemoteDataSource,
    private val preferenceManager: PreferenceManager,
    private val networkManager: NetworkManager
) : SplashRepository {

    override var clientSecretKey: String
        get() = splashLocalDataSource.clientSecretKey
        set(value) {
            splashLocalDataSource.clientSecretKey = value
        }

    override val autoLogin: Boolean
        get() = splashLocalDataSource.autoLogin

    override val tutorial: Boolean
        get() = splashLocalDataSource.tutorial

    override val fcmToken: String?
        get() = splashLocalDataSource.fcmToken

    override fun getAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>> {
        return if (networkManager.networkState()) {
            splashRemoteDataSource.remoteGetAccessToken(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun userSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>> {
        return if (networkManager.networkState()) {
            splashRemoteDataSource.remoteUserSynchronized(data)
                .flatMap {
                    if (it.code() == 200) {
                        userInfoSynchronized(it.body()?.data) //TODO: Non-fatal Exception: java.lang.IllegalStateException
                        Single.just(it)
                    } else {
                        Single.just(it)
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

//    override fun deleteUser(): Completable {
//        return if (networkManager.networkState()) {
//            splashRemoteDataSource.deleteUser()
//                .doOnComplete { preferenceManager.userLogout() }
//        } else {
//            Completable.error(NetworkErrorException("Network Error"))
//        }
//    }

    override fun getFcmToken(): Single<String?> {
        return splashRemoteDataSource.getFcmToken()
            .doOnSuccess { token ->
                token?.let {
                    saveFcmToken(it)
                }
            }
    }

    override fun deleteUser() {
        return splashLocalDataSource.deleteUser()
    }

    private fun saveFcmToken(token: String?) {
        token?.let { splashLocalDataSource.fcmToken = token }
    }
    //TODO:
    private fun userInfoSynchronized(user: SynchronizedDetail?) {
        user?.let {
            with(splashLocalDataSource) {
                name = user.name
                profileImageURL = user.profileImageURL
                nickname = user.nickname
                clientSecretKey = user.clientSecretKey
                fcmToken = user.fcmToken
                accessToken = user.accessToken
                dateOfBirth = user.dateOfBirth
                checkinId = user.checkinId //TODO: Non-fatal Exception: java.lang.IllegalStateException
            }
        }
    }
}