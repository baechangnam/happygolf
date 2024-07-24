package com.artilearn.happygolfgo.data.gamecenter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GameCenterResponse(
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
    val data: GameCenterAll
) {
    data class GameCenterAll(
        @SerializedName("user")
        @Expose
        val user: GameCenterUser,

        @SerializedName("sumary")
        @Expose
        val sumary: GameCenterSumary,

        @SerializedName("averages")
        @Expose
        val averages: GameCenterAverages,

        @SerializedName("nearRankedUsers")
        @Expose
        val rank: List<GameCenterRank>
    )

    data class GameCenterUser(
        @SerializedName("name")
        @Expose
        val name: String,

        @SerializedName("nickname")
        @Expose
        val nickname: String,

        @SerializedName("profileImageURL")
        @Expose
        val profileImageURL: String?
    )

    data class GameCenterSumary(
        @SerializedName("average")
        @Expose
        val average: Float,

        @SerializedName("level")
        @Expose
        val level: String,

        @SerializedName("globalRank")
        @Expose
        val globalRank: Int
    )

    data class GameCenterAverages(
        @SerializedName("driver")
        @Expose
        val driver: Float,

        @SerializedName("woodUtil")
        @Expose
        val woodUtil: Float,

        @SerializedName("iron")
        @Expose
        val iron: Float,

        @SerializedName("shortGame")
        @Expose
        val shortGame: Float,

        @SerializedName("putter")
        @Expose
        val putter: Float
    )

    data class GameCenterRank(
        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("nickname")
        @Expose
        val nickname: String?,

        @SerializedName("globalRank")
        @Expose
        val globalRank: Int,

        @SerializedName("profileImageURL")
        @Expose
        val profileImageURL: String?,

        @SerializedName("average")
        @Expose
        val average: Float,

        @SerializedName("isMe")
        @Expose
        val isMe: Boolean
    )
}