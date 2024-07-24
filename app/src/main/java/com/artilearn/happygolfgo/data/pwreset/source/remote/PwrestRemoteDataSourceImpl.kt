package com.artilearn.happygolfgo.data.pwreset.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import io.reactivex.Completable

class PwrestRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : PwrestRemoteDataSource {

    override fun remoteChangePassword(
        data: HashMap<String, PwrestModel>
    ): Completable {
        return apiInterface.changePassword(data)
    }
}