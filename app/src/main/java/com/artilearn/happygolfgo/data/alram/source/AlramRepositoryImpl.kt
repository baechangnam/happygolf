package com.artilearn.happygolfgo.data.alram.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.alram.AlramResponse
import com.artilearn.happygolfgo.data.alram.source.remote.AlramRemoteDataSource
import com.artilearn.happygolfgo.modules.remoteModule
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class AlramRepositoryImpl(
    private val alramRemoteDataSource: AlramRemoteDataSource,
    private val networkManager: NetworkManager
) : AlramRepository {

    override fun getAlramMessage(): Single<Response<AlramResponse>> {
        return if (networkManager.networkState()) {
            alramRemoteDataSource.remoteGetAlramMessage()
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun deleteAlram(notificationId: Int): Completable {
        return if (networkManager.networkState()) {
            alramRemoteDataSource.deleteAlram(notificationId)
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }

    override fun deleteAll(): Completable {
        return if (networkManager.networkState()) {
            alramRemoteDataSource.deleteAll()
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }
}