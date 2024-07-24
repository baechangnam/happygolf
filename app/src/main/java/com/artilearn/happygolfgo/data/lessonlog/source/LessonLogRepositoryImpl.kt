package com.artilearn.happygolfgo.data.lessonlog.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.lessonlog.LessonLogResponse
import com.artilearn.happygolfgo.data.lessonlog.source.remote.LessonLogRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class LessonLogRepositoryImpl(
    private val lessonLogRemoteDataSource: LessonLogRemoteDataSource,
    private val networkManager: NetworkManager
) : LessonLogRepository {

    override fun lessonLog(): Single<Response<LessonLogResponse>> {
        return if (networkManager.networkState()) {
            lessonLogRemoteDataSource.remoteGetLessonLog()
                .flatMap {
                    with(it) {
                        if (isSuccessful) {
                            Single.just(it)
                        } else {
                            Single.error(HttpException(it))
                        }
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }
}