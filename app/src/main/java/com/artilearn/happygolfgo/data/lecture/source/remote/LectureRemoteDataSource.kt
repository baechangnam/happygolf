package com.artilearn.happygolfgo.data.lecture.source.remote


import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.data.lecture.LectureBookMarkResponse
import com.artilearn.happygolfgo.data.lecture.LectureCommentResponse
import com.artilearn.happygolfgo.data.lecture.LectureDetailResponse
import com.artilearn.happygolfgo.data.lecture.LectureHomeResponse
import com.artilearn.happygolfgo.data.lecture.LectureResponse
import io.reactivex.Single
import retrofit2.Response

interface LectureRemoteDataSource {

    fun getLectureHomeList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureHomeResponse>>

    fun getLectureDetail(lectNo: String): Single<Response<LectureDetailResponse>>
    fun addBookMark(lectNo: String): Single<Response<LectureBookMarkResponse>>
    fun cancelBookMark(lectNo: String): Single<Response<LectureBookMarkResponse>>
    fun addLike(lectNo: String): Single<Response<LectureBookMarkResponse>>
    fun cancelLike(lectNo: String): Single<Response<LectureBookMarkResponse>>
    fun getLectureList(
        keyword: String,
        sectNo: String,
        page: String,
        order: String
    ): Single<Response<LectureResponse>>

    fun getComment(lectNo: String): Single<Response<List<LectureComment>>>
    fun regComment(param : HashMap<String,String>): Single<Response<LectureBookMarkResponse>>

    fun getLikeLectureList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureResponse>>

    fun getPickLectureList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureResponse>>

    fun getCommentMy(): Single<Response<LectureCommentResponse>>


    fun lecturePlayEnd(lectNo: String,param : HashMap<String,String>): Single<Response<LectureBookMarkResponse>>








}