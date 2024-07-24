package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Policy(
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("body")
    @Expose
    val body: String
)