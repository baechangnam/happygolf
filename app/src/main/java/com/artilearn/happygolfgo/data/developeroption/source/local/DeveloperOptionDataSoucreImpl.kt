package com.artilearn.happygolfgo.data.developeroption.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class DeveloperOptionDataSoucreImpl(
    private val preferenceManager: PreferenceManager
) : DeveloperOptionDataSoucre {

    override var environmentMode: String
        get() = preferenceManager.environmentMode
        set(value) {
            preferenceManager.environmentMode = value
        }
}