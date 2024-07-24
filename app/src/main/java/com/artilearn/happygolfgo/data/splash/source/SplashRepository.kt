package com.artilearn.happygolfgo.data.splash.source

import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

interface SplashRepository {

    var clientSecretKey: String
    val autoLogin: Boolean
    val tutorial: Boolean
    val fcmToken: String?

    fun getAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>>

    fun userSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>>

    //fun deleteUser(): Completable

    fun getFcmToken(): Single<String?>

    fun deleteUser()
}