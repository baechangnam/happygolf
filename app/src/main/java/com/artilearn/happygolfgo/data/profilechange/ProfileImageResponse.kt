package com.artilearn.happygolfgo.data.profilechange

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileImageResponse(
    @SerializedName("Data")
    @Expose
    val data: ProfileImage
)

data class ProfileImage(
    @field:SerializedName("imageURL")
    val profile: String
)

