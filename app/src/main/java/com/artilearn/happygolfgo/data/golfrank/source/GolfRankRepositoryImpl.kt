package com.artilearn.happygolfgo.data.golfrank.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.data.golfrank.source.remote.GolfRankRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class GolfRankRepositoryImpl(
    private val remote: GolfRankRemoteDataSource,
    private val network: NetworkManager
) : GolfRankRepository {

    override fun getRanks(): Single<GolfRankResponse.Ranks> {
        return if (network.networkState()) {
            remote.getRanks()
        } else {
            Single.error(NetworkErrorException("Network Error!"))
        }
    }

}