package com.artilearn.happygolfgo.data.myinfo.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class MyInfoLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : MyInfoLocalDataSource {

    override var nickname: String?
        get() = preferenceManager.nickName
        set(value) {
            preferenceManager.nickName = value
        }

    override var name: String
        get() = preferenceManager.name
        set(value) {
            preferenceManager.name = value
        }

    override var profileImageURL: String?
        get() = preferenceManager.profileImageURL
        set(value) {
            preferenceManager.profileImageURL = value
        }

    override var clientSecretKey: String
        get() = preferenceManager.clientScreetKey
        set(value) {
            preferenceManager.clientScreetKey = value
        }

    override var accessToken: String
        get() = preferenceManager.accessToken
        set(value) {
            preferenceManager.accessToken = value
        }
    
    override var fcmToken: String?
        get() = preferenceManager.fcmToken
        set(value) {
            preferenceManager.fcmToken = value
        }
}
    