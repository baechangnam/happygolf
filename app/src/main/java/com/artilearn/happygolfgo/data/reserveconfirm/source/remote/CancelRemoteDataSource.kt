package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import io.reactivex.Completable

interface CancelRemoteDataSource {

    fun remoteReservationPlateCancel(plateId: Int): Completable

    fun remoteReservationLessonCancel(lessonId: Int): Completable
}