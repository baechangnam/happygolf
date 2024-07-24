package com.artilearn.happygolfgo.data.profilechange.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class ProfileChangeLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : ProfileChangeLocalDataSource {
    override var profileImage: String?
        get() = preferenceManager.profileImageURL
        set(value) {
            preferenceManager.profileImageURL = value
        }
}