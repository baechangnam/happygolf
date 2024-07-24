package com.artilearn.happygolfgo.data.pwreset.source

import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import io.reactivex.Completable

interface PwrestRepository {

    val phoneNumber: String

    fun changePassword(
        data: HashMap<String, PwrestModel>
    ): Completable
}