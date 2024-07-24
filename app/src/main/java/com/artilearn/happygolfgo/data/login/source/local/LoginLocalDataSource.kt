package com.artilearn.happygolfgo.data.login.source.local

interface LoginLocalDataSource {

    var name: String
    var profileImageURL: String?
    var nickname: String?
    var clientSecretKey: String
    var accessToken: String
    var autoLogin: Boolean
    var fcmToken: String?
    var phoneNumber: String
    var checkinId: String
}