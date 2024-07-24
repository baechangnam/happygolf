package com.artilearn.happygolfgo.data.version.source

import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.VersionResponse
import com.artilearn.happygolfgo.data.version.source.model.MyPageLocalModel
import io.reactivex.Completable
import io.reactivex.Single

interface VersionRepository {

    fun getAppVersion(
        platform: Int = 1,
        clinentType: Int = 0
    ): Single<VersionResponse>

    fun userLogout(
        data: HashMap<String, LogoutModel>
    ): Completable

    fun getMypageData(): MyPageLocalModel
}