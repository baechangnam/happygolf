package com.artilearn.happygolfgo.data.version.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.VersionResponse
import io.reactivex.Completable
import io.reactivex.Single

class VersionRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : VersionRemoteDataSource {

    override fun remoteGetAppVersion(
        platform: Int,
        clientType: Int
    ): Single<VersionResponse> {
        return apiInterface.getAppVersion()
    }

    override fun userLogout(
        data: HashMap<String, LogoutModel>
    ): Completable {
        return apiInterface.userLogout(data)
    }
}