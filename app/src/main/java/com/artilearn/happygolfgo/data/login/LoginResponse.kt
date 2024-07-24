package com.artilearn.happygolfgo.data.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: LoginData,

    @SerializedName("resultCode")
    @Expose
    val resultCode: Int,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int
)