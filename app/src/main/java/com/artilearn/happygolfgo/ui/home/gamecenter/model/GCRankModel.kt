package com.artilearn.happygolfgo.ui.home.gamecenter.model

data class GCRankModel(
    val globalRank: String,
    val profileImage: String?,
    val nickname: String?,
    val average: String,
    val isMe: Boolean
)