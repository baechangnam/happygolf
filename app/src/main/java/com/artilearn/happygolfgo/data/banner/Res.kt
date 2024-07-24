package com.artilearn.happygolfgo.data.banner

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("announcement") val announcement: Announcement,
    @SerializedName("banners") val banners: List<Banner>
)