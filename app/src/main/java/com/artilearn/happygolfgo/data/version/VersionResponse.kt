package com.artilearn.happygolfgo.data.version

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VersionResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: VersionInfo
)