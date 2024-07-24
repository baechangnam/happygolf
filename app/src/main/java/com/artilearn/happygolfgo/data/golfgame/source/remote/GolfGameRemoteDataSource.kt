package com.artilearn.happygolfgo.data.golfgame.source.remote

import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import io.reactivex.Single

interface GolfGameRemoteDataSource {
    fun getGolfGames(): Single<GolfGameResponse>
}