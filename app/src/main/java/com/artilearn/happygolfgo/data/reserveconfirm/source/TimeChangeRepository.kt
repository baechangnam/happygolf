package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.PlateTimeChangeModel
import com.artilearn.happygolfgo.data.reserveconfirm.PlateTimeChangeResponse
import io.reactivex.Single

interface TimeChangeRepository {
    fun timeChnage(
        hashMap: HashMap<String, PlateTimeChangeModel>
    ): Single<PlateTimeChangeResponse.TimePlateReservation>
}