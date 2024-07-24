package com.artilearn.happygolfgo.data.pwreset.source.remote

import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import io.reactivex.Completable

interface PwrestRemoteDataSource {

    fun remoteChangePassword(
        data: HashMap<String, PwrestModel>
    ): Completable
}