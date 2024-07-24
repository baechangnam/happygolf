package com.artilearn.happygolfgo.data.golfgame.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import io.reactivex.Single

class GolfGameRemoteDataSourceImpl(
    private val api: ApiInterface
) : GolfGameRemoteDataSource {
    override fun getGolfGames(): Single<GolfGameResponse> {
        return api.getGolfGames()
    }
}