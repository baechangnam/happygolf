package com.artilearn.happygolfgo.data.reserveconfirm.source

import io.reactivex.Completable

interface CancelRepository {

    fun reservationPlateCancel(plateId: Int): Completable

    fun reservationLessonCancel(lessonId: Int): Completable
}