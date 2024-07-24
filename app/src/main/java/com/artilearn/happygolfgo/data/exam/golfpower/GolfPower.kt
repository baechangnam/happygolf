package com.artilearn.happygolfgo.data.exam.golfpower

import com.google.gson.annotations.SerializedName

data class GolfPower(
    @SerializedName("delta") val delta: String,
    @SerializedName("indicator") val indicator: String,
    @SerializedName("rankingInAllBranch") val rankingInAllBranch: String,
    @SerializedName("rankingInMyBranch") val rankingInMyBranch: String,
    @SerializedName("score") val score: Int,
    @SerializedName("title") val title: String
)