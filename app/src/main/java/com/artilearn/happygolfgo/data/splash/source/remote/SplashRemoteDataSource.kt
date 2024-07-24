package com.artilearn.happygolfgo.data.splash.source.remote

import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

interface SplashRemoteDataSource {

    fun remoteGetAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>>

    fun remoteUserSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>>

    //fun deleteUser(): Completable

    fun getFcmToken(): Single<String?>
}