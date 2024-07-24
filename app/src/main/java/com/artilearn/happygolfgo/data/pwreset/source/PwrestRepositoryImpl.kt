package com.artilearn.happygolfgo.data.pwreset.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import com.artilearn.happygolfgo.data.pwreset.source.local.PwrestLocalDataSource
import com.artilearn.happygolfgo.data.pwreset.source.remote.PwrestRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Completable

class PwrestRepositoryImpl(
    private val pwrestRemoteDataSource: PwrestRemoteDataSource,
    private val pwrestLocalDataSource: PwrestLocalDataSource,
    private val networkManager: NetworkManager
) : PwrestRepository {

    override val phoneNumber: String
        get() = pwrestLocalDataSource.phoneNumber

    override fun changePassword(
        data: HashMap<String, PwrestModel>
    ): Completable {
        return if (networkManager.networkState()) {
             pwrestRemoteDataSource.remoteChangePassword(data)
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }
}