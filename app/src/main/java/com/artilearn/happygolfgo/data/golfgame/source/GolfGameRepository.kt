package com.artilearn.happygolfgo.data.golfgame.source

import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import io.reactivex.Single

interface GolfGameRepository {
    fun getGolfGames(): Single<GolfGameResponse>
}