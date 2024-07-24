package com.artilearn.happygolfgo.data.lecture.source


import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.LectureComment

import com.artilearn.happygolfgo.data.lecture.LectureBookMarkResponse
import com.artilearn.happygolfgo.data.lecture.LectureCommentResponse
import com.artilearn.happygolfgo.data.lecture.LectureDetailResponse

import com.artilearn.happygolfgo.data.lecture.source.remote.LectureRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response
import com.artilearn.happygolfgo.data.lecture.LectureHomeResponse
import com.artilearn.happygolfgo.data.lecture.LectureResponse

class LectureRepositoryImpl(
    private val lectureRemoteDataSource: LectureRemoteDataSource,
    private val networkManager: NetworkManager
) : LectureRepository {

    override fun getLectureHomeList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureHomeResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getLectureHomeList(keyword, sectNo, page)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getLectureList(
        keyword: String,
        sectNo: String,
        page: String,
        order: String
    ): Single<Response<LectureResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getLectureList(keyword, sectNo, page,order)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getLectureDetail(
        lectNo: String
    ): Single<Response<LectureDetailResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getLectureDetail(lectNo)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun addBookMark(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.addBookMark(lectNo)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun cancelBookMark(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.cancelBookMark(lectNo)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }


    override fun addLike(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.addLike(lectNo)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun cancelLike(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.cancelLike(lectNo)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }


    override fun getComment(
        lectNo: String
    ): Single<Response<List<LectureComment>>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getComment(lectNo)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun regComment(
        param : HashMap<String,String>
    ): Single<Response<LectureBookMarkResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.regComment(param)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }


    override fun getLikeLectureList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getLikeLectureList(keyword, sectNo, page)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getPickLectureList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getPickLectureList(keyword, sectNo, page)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getCommentMy(): Single<Response<LectureCommentResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.getCommentMy()
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun lecturePlayEnd(
        lectNo: String,param : HashMap<String,String>
    ): Single<Response<LectureBookMarkResponse>> {
        return if (networkManager.networkState()) {
            lectureRemoteDataSource.lecturePlayEnd(lectNo, param)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }




}
