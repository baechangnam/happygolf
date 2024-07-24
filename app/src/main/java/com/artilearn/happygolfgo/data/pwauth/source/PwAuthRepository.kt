package com.artilearn.happygolfgo.data.pwauth.source

import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import io.reactivex.Completable

interface PwAuthRepository {
    val phoneNumber: String
    val autoLogin: Boolean

    fun getAuthNumber(
        data: HashMap<String, GetAuthModel>
    ): Completable

    fun confirmAuthNumber(
        data: HashMap<String, ConfirmAuthModel>
    ): Completable
}