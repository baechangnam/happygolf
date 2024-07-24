package com.artilearn.happygolfgo.data.pwreset.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class PwrestLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : PwrestLocalDataSource {

    override val phoneNumber: String
        get() = preferenceManager.phoneNumber
}