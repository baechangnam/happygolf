package com.artilearn.happygolfgo.data.home.source.remote

import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.home.*
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRemoteDataSource {

    fun remoteHomeTicket(): Single<Response<HomeResponse>>

    fun remoteAnnouncementAndExam():Single<Response<AnnouncementAndExamResponse>>

    fun remoteJoinMonthlyExam(data:HashMap<String, JoinMonthlyExamInputModel>): Single<Response<JoinMonthlyExaminationResponse>>

    fun remoteFreshFcmToken(data:HashMap<String, RefreshFcmTokenPutParam>):Single<Response<RefreshFcmTokenResponse>>
    fun remoteSelectReservationList(): Single<Response<ReserveListResponse>>
    fun remoteGetBanners(): Single<Response<BannerDto>>
}