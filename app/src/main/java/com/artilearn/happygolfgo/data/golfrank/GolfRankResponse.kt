package com.artilearn.happygolfgo.data.golfrank

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GolfRankResponse(
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
    val data: Ranks
) {
    data class Ranks(
        @SerializedName("globalRanks")
        @Expose
        val global: List<RankModel>,

        @SerializedName("localRanks")
        @Expose
        val local: List<RankModel>
    )

    data class RankModel(
        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("rank")
        @Expose
        val rank: Int,

        @SerializedName("nickname")
        @Expose
        val nickname: String,

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