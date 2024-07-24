package com.artilearn.happygolfgo.ui.home.gamecenter.mapper

import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCSumaryModel

object SumaryMapper :
    GameCenterMapper<GameCenterResponse.GameCenterSumary, GCSumaryModel> {
    override fun mapper(from: GameCenterResponse.GameCenterSumary): GCSumaryModel {
        return GCSumaryModel(
            average = from.average.toString(),
            level = from.level,
            globalRank = from.globalRank.toString()
        )
    }
}