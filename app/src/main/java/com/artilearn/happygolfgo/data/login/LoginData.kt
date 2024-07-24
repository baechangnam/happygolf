package com.artilearn.happygolfgo.data.login

import com.artilearn.happygolfgo.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("user")
    @Expose
    val user: User
)