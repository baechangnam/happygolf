package com.artilearn.happygolfgo.data.version.source.local

import com.artilearn.happygolfgo.data.version.source.model.MyPageLocalModel
import com.artilearn.happygolfgo.util.PreferenceManager

class VersionLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : VersionLocalDataSource {

    override var appVersion: String
        get() = preferenceManager.appVersion
        set(value) {
            preferenceManager.appVersion = value
        }

    override fun getMypageData(): MyPageLocalModel {
        return preferenceManager.getMypageInfor()
    }
}