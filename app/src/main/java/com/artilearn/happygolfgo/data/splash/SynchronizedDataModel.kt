package com.artilearn.happygolfgo.data.splash

data class SynchronizedDataModel(
    val clientSecretKey: String,
    val fcmToken: String,
    val isPushOn: Int,
    val deviceIdentifier: String,
    val deviceName: String,
    val clientType: Int = 0
)