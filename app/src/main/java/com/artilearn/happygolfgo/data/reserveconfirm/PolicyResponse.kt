package com.artilearn.happygolfgo.data.reserveconfirm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PolicyResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: PolicyInfo
)