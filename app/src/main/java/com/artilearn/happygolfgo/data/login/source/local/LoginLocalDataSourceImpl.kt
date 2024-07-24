package com.artilearn.happygolfgo.data.login.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class LoginLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : LoginLocalDataSource {

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

    override var nickname: String?
        get() = preferenceManager.nickName
        set(value) {
            preferenceManager.nickName = value
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
    override var autoLogin: Boolean
        get() = preferenceManager.autoLogin
        set(value) {
            preferenceManager.autoLogin = value
        }

    override var fcmToken: String?
        get() = preferenceManager.fcmToken
        set(value) {
            preferenceManager.fcmToken = value
        }

    override var phoneNumber: String
        get() = preferenceManager.phoneNumber
        set(value) {
            preferenceManager.phoneNumber = value
        }

    override var checkinId: String
        get() = preferenceManager.checkinId
        set(value) {
            preferenceManager.checkinId = value
        }
}