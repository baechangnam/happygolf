package com.artilearn.happygolfgo.data.golfgame

import com.google.gson.annotations.SerializedName

data class Game(
    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("score")
    val score: Float
)