package com.artilearn.happygolfgo.data.pwauth.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class PwAuthLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : PwAuthLocalDataSource {
    override val autoLogin: Boolean
        get() = preferenceManager.autoLogin

    override val phoneNumber: String
        get() = preferenceManager.phoneNumber
}