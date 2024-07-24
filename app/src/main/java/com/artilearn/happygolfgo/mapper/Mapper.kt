package com.artilearn.happygolfgo.mapper

interface Mapper<in E, out R> {
    fun mapper(item: E): R
}