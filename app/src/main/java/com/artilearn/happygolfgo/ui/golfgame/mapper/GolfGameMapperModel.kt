package com.artilearn.happygolfgo.ui.golfgame.mapper

interface GolfGameMapperModel<in T, out M> {
    fun driverMapper(from: T): M
    fun woodMapper(from: T): M
    fun ironMapper(from: T): M
    fun shortMapper(from: T): M
    fun puttMapper(from: T): M
}