package com.artilearn.happygolfgo.data.tutorial.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class TutorialLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : TutorialLocalDataSource {

    override var tutorial: Boolean
        get() = preferenceManager.tutorial
        set(value) {
            preferenceManager.tutorial = value
        }
}