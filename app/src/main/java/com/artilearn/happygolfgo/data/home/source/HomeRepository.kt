package com.artilearn.happygolfgo.data.home.source

import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.home.*
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {
    fun requestHomeTicket(): Single<Response<HomeResponse>>
    fun requestAnnouncementAndExam(): Single<Response<AnnouncementAndExamResponse>>
    fun requestJoinMonthlyExam(data:HashMap<String, JoinMonthlyExamInputModel>)
            :Single<Response<JoinMonthlyExaminationResponse>>
    fun requestFreshFcmToken(data:HashMap<String, RefreshFcmTokenPutParam>)
            :Single<Response<RefreshFcmTokenResponse>>
    fun selectReservationList(): Single<Response<ReserveListResponse>>
    fun getBanner(): Single<Response<BannerDto>>
}