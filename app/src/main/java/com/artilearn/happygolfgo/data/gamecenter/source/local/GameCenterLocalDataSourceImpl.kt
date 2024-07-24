package com.artilearn.happygolfgo.data.gamecenter.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class GameCenterLocalDataSourceImpl(
    private val pref: PreferenceManager
) : GameCenterLocalDataSource {
    override val name: String
        get() = pref.name

    override val nickName: String?
        get() = pref.nickName

    override val profileImage: String?
        get() = pref.profileImageURL
}