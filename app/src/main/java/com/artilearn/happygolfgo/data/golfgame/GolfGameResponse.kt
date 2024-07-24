package com.artilearn.happygolfgo.data.golfgame

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GolfGameResponse(
    @SerializedName("resultCode")
    @Expose
    val resultCode: Int,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: GolfGameData
) {
    data class GolfGameData(
        @field:SerializedName("driver")
        val driver: Driver,

        @field:SerializedName("iron")
        val iron: Iron,

        @field:SerializedName("putter")
        val putter: Putter,

        @field:SerializedName("shortGame")
        val shortGame: ShortGame,

        @field:SerializedName("woodUtil")
        val woodUtil: WoodUtil
    )
}