package com.artilearn.happygolfgo.data.golfrank.source

import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import io.reactivex.Single

interface GolfRankRepository {
    fun getRanks(): Single<GolfRankResponse.Ranks>
}