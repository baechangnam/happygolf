package com.artilearn.happygolfgo.data.version.source.remote

import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.VersionResponse
import io.reactivex.Completable
import io.reactivex.Single

interface VersionRemoteDataSource {

    fun remoteGetAppVersion(
        platform: Int = 1,
        clientType: Int = 0
    ): Single<VersionResponse>

    fun userLogout(
        data: HashMap<String, LogoutModel>
    ): Completable
}