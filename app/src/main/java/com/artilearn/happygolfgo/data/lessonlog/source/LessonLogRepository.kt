package com.artilearn.happygolfgo.data.lessonlog.source

import com.artilearn.happygolfgo.data.lessonlog.LessonLogResponse
import io.reactivex.Single
import retrofit2.Response

interface LessonLogRepository {

    fun lessonLog(): Single<Response<LessonLogResponse>>
}