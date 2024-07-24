package com.artilearn.happygolfgo.data.version.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.VersionResponse
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSource
import com.artilearn.happygolfgo.data.version.source.model.MyPageLocalModel
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import com.artilearn.happygolfgo.util.PreferenceManager
import io.reactivex.Completable
import io.reactivex.Single

class VersionRepositoryImpl(
    private val versionRemoteDataSource: VersionRemoteDataSource,
    private val versionLocalDataSource: VersionLocalDataSource,
    private val preferenceManager: PreferenceManager,
    private val networkManager: NetworkManager
) : VersionRepository {

    override fun getAppVersion(
        platform: Int,
        clinentType: Int
    ): Single<VersionResponse> {
       return if (networkManager.networkState()) {
           versionRemoteDataSource.remoteGetAppVersion()
               .flatMap {
                   versionLocalDataSource.appVersion = it.data.latestVersion
                   Single.just(it)
               }
       } else {
           Single.error(NetworkErrorException("Network Error"))
       }
    }

    override fun userLogout(
        data: HashMap<String, LogoutModel>
    ): Completable {
        return if (networkManager.networkState()) {
            versionRemoteDataSource.userLogout(data)
                .doOnComplete { preferenceManager.userLogout() }
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getMypageData(): MyPageLocalModel {
        return versionLocalDataSource.getMypageData()
    }
}