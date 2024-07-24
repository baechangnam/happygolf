package com.artilearn.happygolfgo.ui.home.gamecenter.mapper

import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCAveragesModel

object AveragesMapper
    : GameCenterMapper<GameCenterResponse.GameCenterAverages, GCAveragesModel> {
    override fun mapper(from: GameCenterResponse.GameCenterAverages): GCAveragesModel {
        return GCAveragesModel(
            driver = from.driver.toString(),
            woodUtil = from.woodUtil.toString(),
            iron = from.iron.toString(),
            shortGame = from.shortGame.toString(),
            putter = from.putter.toString()
        )
    }
}