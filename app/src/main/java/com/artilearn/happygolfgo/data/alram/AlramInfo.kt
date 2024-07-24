package com.artilearn.happygolfgo.data.alram

import com.artilearn.happygolfgo.data.Alram
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AlramInfo(
    @SerializedName("notifications")
    @Expose
    val notifications: List<Alram>
)