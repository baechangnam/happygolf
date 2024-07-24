package com.artilearn.happygolfgo.data.home.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.home.*
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import io.reactivex.Single
import retrofit2.Response

class HomeRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : HomeRemoteDataSource {

    override fun remoteHomeTicket(): Single<Response<HomeResponse>> {
        return apiInterface.mainHome()
    }

    override fun remoteAnnouncementAndExam(): Single<Response<AnnouncementAndExamResponse>> {
        return apiInterface.announcementAndExam()
    }

    override fun remoteJoinMonthlyExam( data: HashMap<String, JoinMonthlyExamInputModel>)
    :Single<Response<JoinMonthlyExaminationResponse>> {
        return apiInterface.joinMonthlyExam(data)
    }

    override fun remoteFreshFcmToken(data: HashMap<String, RefreshFcmTokenPutParam>): Single<Response<RefreshFcmTokenResponse>> {
        return apiInterface.refreshFcmToken(data)
    }

    override fun remoteSelectReservationList(): Single<Response<ReserveListResponse>> {
        return apiInterface.reservationList()
    }

    override fun remoteGetBanners(): Single<Response<BannerDto>> {
        return apiInterface.getBanners()
    }
}