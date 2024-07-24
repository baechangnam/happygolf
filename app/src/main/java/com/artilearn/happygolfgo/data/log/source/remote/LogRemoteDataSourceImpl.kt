package com.artilearn.happygolfgo.data.log.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.log.LogResponse
import io.reactivex.Single
import retrofit2.Response

class LogRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : LogRemoteDataSource {

    override fun remoteLessonLogDetail(
        lessonId: Int
    ): Single<Response<LogResponse>> {
        return apiInterface.lessonLogDetail(lessonId)
    }
}