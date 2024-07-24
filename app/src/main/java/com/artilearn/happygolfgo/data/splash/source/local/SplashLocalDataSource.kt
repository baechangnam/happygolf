package com.artilearn.happygolfgo.data.splash.source.local

interface SplashLocalDataSource {

    var accessToken: String
    var clientSecretKey: String
    val autoLogin: Boolean
    val tutorial: Boolean
    var name: String
    var profileImageURL: String?
    var nickname: String?
    var dateOfBirth: String
    var fcmToken: String?
    var checkinId: String

    fun deleteUser()

}