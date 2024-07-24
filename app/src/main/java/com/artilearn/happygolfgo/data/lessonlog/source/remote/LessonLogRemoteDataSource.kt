package com.artilearn.happygolfgo.data.lessonlog.source.remote

import com.artilearn.happygolfgo.data.lessonlog.LessonLogResponse
import io.reactivex.Single
import retrofit2.Response

interface LessonLogRemoteDataSource {

    fun remoteGetLessonLog(): Single<Response<LessonLogResponse>>
}