package com.artilearn.happygolfgo.data.login.source.remote

import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import io.reactivex.Single

interface LoginRemoteDataSource {

    fun remoteLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>

    fun getFcmToken(): Single<String?>
}