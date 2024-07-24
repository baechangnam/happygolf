package com.artilearn.happygolfgo.data.reservelist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReserveListResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: ReserveListInfo
)