package com.artilearn.happygolfgo.ui.home.record.mapper

interface TrainingRecordMapper<in T, out R> {
    fun mapper(from: T): R
}