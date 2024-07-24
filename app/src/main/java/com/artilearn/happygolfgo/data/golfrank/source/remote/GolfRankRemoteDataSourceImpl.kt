package com.artilearn.happygolfgo.data.golfrank.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import io.reactivex.Single

class GolfRankRemoteDataSourceImpl(
    private val api: ApiInterface
) : GolfRankRemoteDataSource {
    override fun getRanks(): Single<GolfRankResponse.Ranks> {
        return api.getGameRanks().map { it.data }
    }
}