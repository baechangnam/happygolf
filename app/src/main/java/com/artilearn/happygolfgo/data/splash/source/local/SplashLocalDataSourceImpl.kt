package com.artilearn.happygolfgo.data.splash.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class SplashLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : SplashLocalDataSource {

    override val autoLogin: Boolean
        get() = preferenceManager.autoLogin

    override val tutorial: Boolean
        get() = preferenceManager.tutorial

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

    override var dateOfBirth: String
        get() = preferenceManager.dateOfBirth
        set(value) {
            preferenceManager.dateOfBirth = value
        }

    override var fcmToken: String?
        get() = preferenceManager.fcmToken
        set(value) {
            preferenceManager.fcmToken = value
        }
    //TODO: non-fatal exception in firebase
    override var checkinId: String
        get() = preferenceManager.checkinId
        set(value) {
            preferenceManager.checkinId = value
        }

    override fun deleteUser() {
        return preferenceManager.userLogout()
    }
}