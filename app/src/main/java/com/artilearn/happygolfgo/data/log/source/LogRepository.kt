package com.artilearn.happygolfgo.data.log.source

import com.artilearn.happygolfgo.data.log.LogResponse
import io.reactivex.Single
import retrofit2.Response

interface LogRepository {

    fun lessonLogDetail(
        lessonId: Int
    ): Single<Response<LogResponse>>
}