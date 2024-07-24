package com.artilearn.happygolfgo.data.reservelist.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.data.reservelist.source.remote.ReserveListRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class ReserveListRepositoryImpl(
    private val reserveListRemoteDataSource: ReserveListRemoteDataSource,
    private val networkManager: NetworkManager
) : ReserveListRepository {

    override fun selectReservationList(): Single<Response<ReserveListResponse>> {
        return if (networkManager.networkState()) {
            reserveListRemoteDataSource.remoteSelectReservationList()
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

    override fun selectReservationListInMultiBranch(): Single<Response<ReserveListResponse>> {
        return if (networkManager.networkState()) {
            reserveListRemoteDataSource.remoteSelectReservationListInMultiBranche()
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
}