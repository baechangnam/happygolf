package com.artilearn.happygolfgo.ui.home.gamecenter.mapper

interface GameCenterMapper<in T, out R> {
    fun mapper(from: T): R
}