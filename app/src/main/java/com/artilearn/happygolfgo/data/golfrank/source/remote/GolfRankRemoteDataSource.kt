package com.artilearn.happygolfgo.data.golfrank.source.remote

import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import io.reactivex.Single

interface GolfRankRemoteDataSource {
    fun getRanks(): Single<GolfRankResponse.Ranks>
}