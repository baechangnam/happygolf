package com.artilearn.happygolfgo.data.pwauth.source.remote

import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import io.reactivex.Completable

interface PwAuthRemoteDataSource {

    fun remoteGetAuthNumber(
        data: HashMap<String, GetAuthModel>
    ): Completable

    fun remoteConfirmAuthNumber(
        data: HashMap<String, ConfirmAuthModel>
    ): Completable
}