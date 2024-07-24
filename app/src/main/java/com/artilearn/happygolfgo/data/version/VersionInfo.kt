package com.artilearn.happygolfgo.data.version

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VersionInfo(
    @SerializedName("latestVersion")
    @Expose
    val latestVersion: String
)