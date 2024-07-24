package com.artilearn.happygolfgo.data.pwauth.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import io.reactivex.Completable

class PwAuthRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : PwAuthRemoteDataSource {

    override fun remoteGetAuthNumber(
        data: HashMap<String, GetAuthModel>
    ): Completable {
        return apiInterface.getAuthNumber(data)
    }

    override fun remoteConfirmAuthNumber(
        data: HashMap<String, ConfirmAuthModel>
    ): Completable {
        return apiInterface.confirmAuthNumber(data)
    }
}