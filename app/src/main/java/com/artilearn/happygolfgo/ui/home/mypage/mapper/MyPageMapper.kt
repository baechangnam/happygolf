package com.artilearn.happygolfgo.ui.home.mypage.mapper

import com.artilearn.happygolfgo.data.version.source.model.MyPageLocalModel
import com.artilearn.happygolfgo.ui.home.mypage.model.MyPage

object MyPageMapper : MyPageMapperModel<MyPageLocalModel, MyPage> {
    override fun myPageMapper(from: MyPageLocalModel): MyPage {
        return MyPage(
            nameAndNickName = when {
                from.name.isEmpty() && from.nickname.isEmpty() -> "이름 없음 / 닉네임 없음"
                from.name.isEmpty() && from.nickname.isNotEmpty() -> "이름 없음 / ${from.nickname}"
                from.name.isNotEmpty() && from.nickname.isEmpty() -> "${from.name} / 닉네임 없음"
                else -> "${from.name} / ${from.nickname}"
            },
            checkinId = if (from.checkinId.isEmpty()) {
                "체크인 번호 없음"
            } else {
                "체크인 번호 : ${from.checkinId}"
            },
            profileImage = if (from.profileImage.isNullOrEmpty()) {
                null
            } else {
                from.profileImage
            }
        )
    }
}