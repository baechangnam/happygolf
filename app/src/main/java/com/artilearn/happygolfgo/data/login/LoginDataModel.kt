package com.artilearn.happygolfgo.data.login

data class LoginDataModel(
    val phoneNumber: String,
    val password: String,
    val fcmToken: String?,
    val deviceIdentifier: String,
    val deviceName: String,
    val branchID: Int? = null,
    val operatingSystem: Int = 1,
    val sysLang: Int = 0,
    val systemLanguage: Int = 0
)