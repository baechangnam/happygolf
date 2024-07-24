package com.artilearn.happygolfgo.ui.golfrank.model

data class GolfRankModel(
    val rank: String,
    val profileImage: String?,
    val nickName: String?,
    val averages: String,
    val isMe: Boolean
)