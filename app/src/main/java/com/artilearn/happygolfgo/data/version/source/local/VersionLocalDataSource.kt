package com.artilearn.happygolfgo.data.version.source.local

import com.artilearn.happygolfgo.data.version.source.model.MyPageLocalModel

interface VersionLocalDataSource {

    var appVersion: String

    fun getMypageData(): MyPageLocalModel
}