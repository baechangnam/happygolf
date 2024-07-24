package com.artilearn.happygolfgo.data.myinfo.source.local

interface MyInfoLocalDataSource {

    var nickname: String?
    var name: String
    var profileImageURL: String?
    var clientSecretKey: String
    var accessToken: String
    var fcmToken: String?
}