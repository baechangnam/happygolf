package com.artilearn.happygolfgo.data.log.source.remote

import com.artilearn.happygolfgo.data.log.LogResponse
import io.reactivex.Single
import retrofit2.Response

interface LogRemoteDataSource {

    fun remoteLessonLogDetail(
        lessonId: Int
    ): Single<Response<LogResponse>>
}