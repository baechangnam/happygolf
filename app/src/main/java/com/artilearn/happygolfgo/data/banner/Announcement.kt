package com.artilearn.happygolfgo.data.banner

import com.google.gson.annotations.SerializedName

data class Announcement(
    @SerializedName("issuedDate") val issuedDate: String = "",
    @SerializedName("serviceUrl") val serviceUrl: String = "",
    @SerializedName("subject") val subject: String = ""
)