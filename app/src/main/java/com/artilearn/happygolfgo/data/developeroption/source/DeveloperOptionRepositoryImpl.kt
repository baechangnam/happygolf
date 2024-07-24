package com.artilearn.happygolfgo.data.developeroption.source

import com.artilearn.happygolfgo.data.developeroption.source.local.DeveloperOptionDataSoucre

class DeveloperOptionRepositoryImpl(
    private val developerOptionDataSoucre: DeveloperOptionDataSoucre
) : DeveloperOptionRepository {

    override var environmentMode: String
        get() = developerOptionDataSoucre.environmentMode
        set(value) {
            developerOptionDataSoucre.environmentMode = value
        }
}