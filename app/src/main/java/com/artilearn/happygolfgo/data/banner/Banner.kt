package com.artilearn.happygolfgo.data.banner

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("isOpenInNewWindow") val isOpenInNewWindow: String,
    @SerializedName("openUrl") val openUrl: String
)