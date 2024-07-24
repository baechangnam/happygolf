package com.artilearn.happygolfgo.data.golfgame

import com.google.gson.annotations.SerializedName

data class ShortGame(
    @field:SerializedName("average")
    val average: Float,

    @field:SerializedName("bestScore")
    val bestScore: Int,

    @field:SerializedName("games")
    val games: List<Game>
)