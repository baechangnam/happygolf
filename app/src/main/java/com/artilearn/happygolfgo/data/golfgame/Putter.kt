package com.artilearn.happygolfgo.data.golfgame

import com.google.gson.annotations.SerializedName

data class Putter(
    @field:SerializedName("average")
    val average: String,

    @field:SerializedName("bestScore")
    val bestScore: Int,

    @field:SerializedName("games")
    val games: List<Game>
)