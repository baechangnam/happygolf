package com.artilearn.happygolfgo.data.lessonlog.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.lessonlog.LessonLogResponse
import io.reactivex.Single
import retrofit2.Response

class LessonLogRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : LessonLogRemoteDataSource {

    override fun remoteGetLessonLog(): Single<Response<LessonLogResponse>> {
        return apiInterface.getLessonLogs()
    }
}