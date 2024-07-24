package com.artilearn.happygolfgo.base

interface Identifiable {
    val identifier: Any

    override operator fun equals(other: Any?): Boolean
}