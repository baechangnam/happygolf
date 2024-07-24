package com.artilearn.happygolfgo.data.pwauth

data class GetAuthModel(
    val phoneNumber: String,
    val clientType: Int = 0
)