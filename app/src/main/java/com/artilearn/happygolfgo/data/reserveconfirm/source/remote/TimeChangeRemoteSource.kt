package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.data.reserveconfirm.PlateTimeChangeModel
import com.artilearn.happygolfgo.data.reserveconfirm.PlateTimeChangeResponse
import io.reactivex.Single
import retrofit2.Response

interface TimeChangeRemoteSource {
    fun plateTimeChange(
        data: HashMap<String, PlateTimeChangeModel>
    ): Single<Response<PlateTimeChangeResponse>>
}