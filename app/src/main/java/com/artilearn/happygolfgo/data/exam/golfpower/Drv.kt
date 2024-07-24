package com.artilearn.happygolfgo.data.exam.golfpower

import com.google.gson.annotations.SerializedName

data class Drv(
    @SerializedName("delta") val delta: String,
    @SerializedName("indicator") val indicator: String,
    @SerializedName("rankingInAllBranch") val rankingInAllBranch: String,
    @SerializedName("rankingInMyBranch") val rankingInMyBranch: String,
    @SerializedName("score") val score: String,
    @SerializedName("title") val title: String
)