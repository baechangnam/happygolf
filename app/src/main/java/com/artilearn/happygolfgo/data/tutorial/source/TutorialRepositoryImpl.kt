package com.artilearn.happygolfgo.data.tutorial.source

import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSource

class TutorialRepositoryImpl(
    private val tutorialLocalDataSource: TutorialLocalDataSource
) : TutorialRepository {

    override var tutorial: Boolean
        get() = tutorialLocalDataSource.tutorial
        set(value) {
            tutorialLocalDataSource.tutorial = value
        }
}