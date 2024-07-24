package com.artilearn.happygolfgo.api


import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.data.home.*
import com.artilearn.happygolfgo.data.lecture.LectureBookMarkResponse
import com.artilearn.happygolfgo.data.lecture.LectureCommentResponse
import com.artilearn.happygolfgo.data.lecture.LectureDetailResponse
import com.artilearn.happygolfgo.data.reserveconfirm.*
import com.artilearn.happygolfgo.data.lecture.LectureHomeResponse
import com.artilearn.happygolfgo.data.lecture.LectureResponse

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface LectureApiInterface {

    @Headers("Content-Type: application/json")
    @GET("api/sect/lect")
    fun getLectureHomeList(
        @Query("keyword") keyword: String,
        @Query("sectNo") sectNo: String,
        @Query("page") page: String
    ): Single<Response<LectureHomeResponse>>


    @Headers("Content-Type: application/json")
    @GET("api/lect")
    fun getLectureList(
        @Query("keyword") keyword: String,
        @Query("sectNo") sectNo: String,
        @Query("page") page: String,
        @Query("order") order: String
    ): Single<Response<LectureResponse>>

    @Headers("Content-Type: application/json")
    @GET("api/lect/{lectNo}")
    fun getLectDetail(
        @Path(value = "lectNo") lectNo: String
    ): Single<Response<LectureDetailResponse>>


    @Headers("Content-Type: application/json")
    @POST("api/lect/{lectNo}/mypick")
    fun addBookMark(
        @Path(value = "lectNo") lectNo: String
    ): Single<Response<LectureBookMarkResponse>>

    @Headers("Content-Type: application/json")
    @DELETE("api/lect/{lectNo}/mypick")
    fun cancelBookMark(
        @Path(value = "lectNo") lectNo: String
    ): Single<Response<LectureBookMarkResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/lect/{lectNo}/like")
    fun addLike(
        @Path(value = "lectNo") lectNo: String
    ): Single<Response<LectureBookMarkResponse>>

    @Headers("Content-Type: application/json")
    @DELETE("api/lect/{lectNo}/cancel-like")
    fun cancelLike(
        @Path(value = "lectNo") lectNo: String
    ): Single<Response<LectureBookMarkResponse>>

    @Headers("Content-Type: application/json")
    @GET("api/cmmt/{lectNo}")
    fun getComment(
        @Path(value = "lectNo") lectNo: String
    ): Single<Response<List<LectureComment>>>


    @Headers("Content-Type: application/json")
    @POST("api/cmmt")
    fun regComment(
        @Body param : HashMap<String,String>
    ): Single<Response<LectureBookMarkResponse>>


    @Headers("Content-Type: application/json")
    @GET("api/lect/mylike")
    fun getLikeLectureList(
        @Query("keyword") keyword: String,
        @Query("sectNo") sectNo: String,
        @Query("page") page: String
    ): Single<Response<LectureResponse>>

    @Headers("Content-Type: application/json")
    @GET("api/lect/mypick")
    fun getPickLectureList(
        @Query("keyword") keyword: String,
        @Query("sectNo") sectNo: String,
        @Query("page") page: String
    ): Single<Response<LectureResponse>>

    @Headers("Content-Type: application/json")
    @GET("api/cmmt/usr/my")
    fun getCommentMy(): Single<Response<LectureCommentResponse>>


    @Headers("Content-Type: application/json")
    @PUT("api/lect/{lectNo}/play/done")
    fun lecturePlayEnd(
        @Path(value = "lectNo") lectNo: String ,   @Body playTime : HashMap<String,String>
    ): Single<Response<LectureBookMarkResponse>>



}
