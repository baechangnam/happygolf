package com.artilearn.happygolfgo.data.golfgame.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import com.artilearn.happygolfgo.data.golfgame.source.remote.GolfGameRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class GolfGameRepositoryImpl(
    private val remote: GolfGameRemoteDataSource,
    private val network: NetworkManager
) : GolfGameRepository {
    override fun getGolfGames(): Single<GolfGameResponse> {
        return if (network.networkState()) {
            remote.getGolfGames()
        } else {
            Single.error(NetworkErrorException("Network Error!"))
        }
    }
}