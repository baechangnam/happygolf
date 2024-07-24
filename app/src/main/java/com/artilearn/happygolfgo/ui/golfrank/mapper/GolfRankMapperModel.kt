package com.artilearn.happygolfgo.ui.golfrank.mapper

interface GolfRankMapperModel<in T, out M> {
    fun localMapper(from: T): M
    fun globalMapper(from: T): M
}