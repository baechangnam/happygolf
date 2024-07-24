package com.artilearn.happygolfgo.data.pwauth.source.local

interface PwAuthLocalDataSource {
    val autoLogin: Boolean
    val phoneNumber: String
}