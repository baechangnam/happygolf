package com.artilearn.happygolfgo.data.exam.golfpower

import com.google.gson.annotations.SerializedName

data class Avg(
    @SerializedName("delta") val delta: String,
    @SerializedName("indicator") val indicator: String,
    @SerializedName("numberOfPlayersInAllBranch") val numberOfPlayersInAllBranch: String,
    @SerializedName("numberOfPlayersInMyBranch") val numberOfPlayersInMyBranch: String,
    @SerializedName("rankingInAllBranch") val rankingInAllBranch: String,
    @SerializedName("rankingInMyBranch") val rankingInMyBranch: String,
    @SerializedName("score") val score: String,
    @SerializedName("title") val title: String
)