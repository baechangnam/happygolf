package com.artilearn.happygolfgo.ui.golfgame.mapper

import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import com.artilearn.happygolfgo.ui.golfgame.model.GolfGameModel

object GolfGameMapper
    : GolfGameMapperModel<GolfGameResponse.GolfGameData, GolfGameModel> {

    override fun driverMapper(from: GolfGameResponse.GolfGameData): GolfGameModel {
        return GolfGameModel(
            club = "드라이버",
            averageScore = from.driver.average.toString(),
            highScore = from.driver.bestScore.toString(),
            scores = from.driver.games.map { game ->
                game.score
            },
            dates = from.driver.games.map { date ->
                date.createdAt
            }
        )
    }

    override fun woodMapper(from: GolfGameResponse.GolfGameData): GolfGameModel {
        return GolfGameModel(
            club = "우드",
            averageScore = from.woodUtil.average.toString(),
            highScore = from.woodUtil.bestScore.toString(),
            scores = from.woodUtil.games.map { game ->
                game.score
            },
            dates = from.woodUtil.games.map { date ->
                date.createdAt
            }
        )
    }

    override fun ironMapper(from: GolfGameResponse.GolfGameData): GolfGameModel {
        return GolfGameModel(
            club = "아이언",
            averageScore = from.iron.average.toString(),
            highScore = from.iron.bestScore.toString(),
            scores = from.iron.games.map { game ->
                game.score
            },
            dates = from.iron.games.map { date ->
                date.createdAt
            }
        )
    }

    override fun shortMapper(from: GolfGameResponse.GolfGameData): GolfGameModel {
        return GolfGameModel(
            club = "숏 아이언 / 웻지",
            averageScore = from.iron.average.toString(),
            highScore = from.iron.bestScore.toString(),
            scores = from.iron.games.map { game ->
                game.score
            },
            dates = from.iron.games.map { date ->
                date.createdAt
            }
        )
    }

    override fun puttMapper(from: GolfGameResponse.GolfGameData): GolfGameModel {
        return GolfGameModel(
            club = "퍼팅",
            averageScore = from.putter.average,
            highScore = from.putter.bestScore.toString(),
            scores = from.putter.games.map { game ->
                game.score
            },
            dates = from.putter.games.map { date ->
                date.createdAt
            }
        )
    }

}