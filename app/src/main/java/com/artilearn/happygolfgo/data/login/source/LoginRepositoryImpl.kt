package com.artilearn.happygolfgo.data.login.source

import android.accounts.NetworkErrorException
import android.util.Log
import com.artilearn.happygolfgo.data.User
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class LoginRepositoryImpl(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val networkManager: NetworkManager
) : LoginRepository {

    override var autoLogin: Boolean
        get() = loginLocalDataSource.autoLogin
        set(value) {
            loginLocalDataSource.autoLogin = value
        }

    override val tutorial: Boolean
        get() = loginLocalDataSource.autoLogin

    override var fcmToken: String?
        get() = loginLocalDataSource.fcmToken
        set(value) {
            loginLocalDataSource.fcmToken = value
        }

    override fun repositoryLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return if (networkManager.networkState()) {
            loginRemoteDataSource.remoteLogin(data)
                .flatMap {
                    if (it.data.user.overlapUser == null) {
                        saveLocalData(it.data.user)
                        Single.just(it)
                    } else {
                        Single.just(it)
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getFcmToken(): Single<String?> {
        return loginRemoteDataSource.getFcmToken()
            .doOnSuccess { saveFcmToken(it) }
    }

    private fun saveLocalData(user: User) {
        with(loginLocalDataSource) {
            accessToken = user.accessToken
            clientSecretKey = user.clientSecretKey
            name = user.name
            profileImageURL = user.profileImageURL
            autoLogin = true
            nickname = user.nickname
            phoneNumber = user.phoneNumber
            //DONETODO:  알수 없는 오류 해결중: checkinID가 넘어오지 않을 때가 있다 Version 41(2.2.5).
//            checkinId =user.checkinId
            checkinId = user.checkinId ?: "0000"
        }
    }

    private fun saveFcmToken(token: String?) {
        token?.let { loginLocalDataSource.fcmToken = token }
    }
}