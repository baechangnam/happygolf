package com.artilearn.happygolfgo.ui.home.mypage.mapper

interface MyPageMapperModel<in T, out M> {
    fun myPageMapper(from: T): M
}