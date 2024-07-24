package com.artilearn.happygolfgo.api

import com.artilearn.happygolfgo.data.alram.AlramResponse
import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.exam.ExamDto
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPowerExamResultDto
import com.artilearn.happygolfgo.data.exam.state.ExamStateDto
import com.artilearn.happygolfgo.data.exam.state.request.ExamStateBody
import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.data.home.*
import com.artilearn.happygolfgo.data.lessonlog.LessonLogResponse
import com.artilearn.happygolfgo.data.log.LogResponse
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoResponse
import com.artilearn.happygolfgo.data.profilechange.ProfileImageResponse
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import com.artilearn.happygolfgo.data.reserveconfirm.*
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import com.artilearn.happygolfgo.data.ticketmanager.TicketManagerResponse
import com.artilearn.happygolfgo.data.tickettime.TicketTimeResponse
import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.VersionResponse
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @POST("auth/user/doSigninBranch")
    fun login(
        @Body data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>

    @POST("auth/common/renewAccessToken")
    fun getAccessToken(
        @Body data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>>

    @POST("auth/common/update")
    fun userSynchronized(
        @Body data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>>

    //DONETODO:   main/user/ver_1_0 : 최대 예약 걸려 있을때, 무시하고 진행할 수 있는 옵션 저장 한다. 2022.02.13
    //@GET("main/user")
    //doneTODO: 제주 브랜치 기존 ver_1_0 에서  multibranchtickets로 변경
//    @GET("main/user/ver_1_0")
//    fun mainHome(): Single<Response<HomeResponse>>

    @GET("main/user/multibranchtickets")
    fun mainHome(): Single<Response<HomeResponse>>

    @GET("main/user/announcementAndExam")
    fun announcementAndExam(
        @Query("mobilePlatform") mobilePlatform: String = "android",
        @Query("appVersion") appVersion: String = "2.2.10"
    ): Single<Response<AnnouncementAndExamResponse>>

    @POST("main/user/joinMonthlyExamination")
    fun joinMonthlyExam(
        @Body data: HashMap<String, JoinMonthlyExamInputModel>
    ): Single<Response<JoinMonthlyExaminationResponse>>

    @POST("auth/user/refreshFcmToken")
    fun refreshFcmToken(
        @Body data: HashMap<String, RefreshFcmTokenPutParam>
    ): Single<Response<RefreshFcmTokenResponse>>

    @GET("mypage/appversion")
    fun getAppVersion(
        @Query("platform") platform: Int = 1,
        @Query("clientType") clientType: Int = 0
    ): Single<VersionResponse>

    @PUT("mypage/profile")
    fun updateProfile(
        @Body data: HashMap<String, MyInfoModel>
    ): Single<MyInfoResponse>

    @POST("auth/common/verification/send")
    fun getAuthNumber(
        @Body data: HashMap<String, GetAuthModel>
    ): Completable

    @POST("auth/common/verification/check")
    fun confirmAuthNumber(
        @Body data: HashMap<String, ConfirmAuthModel>
    ): Completable

    @PUT("auth/user/changePassword")
    fun changePassword(
        @Body data: HashMap<String, PwrestModel>
    ): Completable

    @GET("user/lessonlogs")
    fun getLessonLogs(): Single<Response<LessonLogResponse>>

    //doneTODO: 2022/0304/multibranchtickets notification handling
//    @GET("main/user/notifications")
    @GET("main/user/notifications_v_1_0")
    fun getAlramMessage(): Single<Response<AlramResponse>>

    @GET("reservation/user/plateAvailableDates/{ticketID}")
    fun selectPlateDate(
        @Path("ticketID") ticketId: Int
    ): Single<Response<TicketDateResponse>>

    @GET("reservation/user/lessonAvailableDates/{ticketID}")
    fun selectLessonDate(
        @Path("ticketID") ticketId: Int
    ): Single<Response<TicketDateResponse>>

    @GET("reservation/user/plateAvailability")
    fun selectPlateTime(
        @Query("startDate") startDate: String,
        @Query("ticketID") ticketId: Int
    ): Single<Response<TicketTimeResponse>>

    @GET("reservation/user/lessonAvailability")
    fun selectLessonTime(
        @Query("startDate") startDate: String,
        @Query("ticketID") ticketId: Int
    ): Single<Response<TicketTimeResponse>>

    @GET("reservation/user/plate/policy")
    fun requestPolicy(): Single<Response<PolicyResponse>>

    @POST("reservation/user/plate")
    fun reservationPlate(
        @Body data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>>


    @POST("reservation/user/plate/threeOneTime")
    fun reservationThreeOneTimePlate(
        @Body data: HashMap<String, ReserveThreeOneTimePlateModel>
    ): Single<Response<ReservePlateResponse>>


    @POST("reservation/user/lessonReservation")
    fun reservationLesson(
        @Body data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>>

//    @GET("reservation/user/myReservationsInMultiBranch")
//    fun reservationListInMultiBranch(): Single<Response<ReserveListResponse>>

    //doneTODO: multiBranchCall
    @GET("reservation/user/myReservationsInMultiBranch")
    fun reservationListInMultiBranch(): Single<Response<ReserveListResponse>>

    // 내 예약 목록
    //doneTODO: 다중 브랜치 사용자 로그인 추가
//    @GET("reservation/user/myReservations")
    @GET("reservation/user/myReservations_v_1")
    fun reservationList(): Single<Response<ReserveListResponse>>

    @DELETE("reservation/user/lessonReservation/{lessonReservationID}")
    fun reservationLessonCancel(
        @Path("lessonReservationID") lessonId: Int
    ): Completable

    @DELETE("reservation/user/plateReservation/{plateReservationID}")
    fun reservationPlateCancel(
        @Path("plateReservationID") plateId: Int
    ): Completable

    @GET("user/lessonlogs/detail/{id}")
    fun lessonLogDetail(
        @Path("id") lessonLogId: Int
    ): Single<Response<LogResponse>>

    @PUT("user/lessonlogs")
    fun uploadLessonComment(
        @Body data: HashMap<String, CommentModel>
    ): Completable
    //doneTODO:  지점 정보 추가
    @GET("reservation/user/lessonReservation/detail/{lessonReservationID}")
    fun getReservationLessonDetail(
        @Path("lessonReservationID") lessonId: Int
    ): Single<Response<ReservationLessonDetail>>
    //doneTODO: 지점 정보 추가
    @GET("reservation/user/plateReservation/detail/{plateReservationID}")
    fun getReservationPlateDetail(
        @Path("plateReservationID") plateId: Int
    ): Single<Response<ReservationPlateDetail>>

    @PUT("auth/user/doSignout")
    fun userLogout(
        @Body data: HashMap<String, LogoutModel>
    ): Completable

    @POST("reservation/user/plateReservation/changeTime")
    fun plateTimeChange(
        @Body data: HashMap<String, PlateTimeChangeModel>
    ): Single<Response<PlateTimeChangeResponse>>

    //comments: API 추가  vc45
    //vc45 trainingManagement/summary/time
    @GET("trainingCenter/summary/time")
    fun getTrainingManagementSummaryTime(): Single<Response<TrainingManagementSummaryTimeResponse>>

    @GET("main/user/announcementAndExam/myGolfPowerPageExamResultPageInfo")
    fun getMyGolfPowerExamResultPageInfo(
        @Query("mobilePlatform") mobilePlatform: String = "android",
    ): Single<Response<MyGolfPowerExamResultPageInfoResponse>>

    @GET("main/user/announcementAndExam/myGolfPowerPageExamResultPageInfoDetail/{pageIndex}")
    fun getMyGolfPowerPageExamResultPageInfoDetail(
       @Path("pageIndex")  pageIndex: Int
    ):Single<Response<MyGolfPowerExamResultPageInfoDetailResponse>>

    @GET("main/user/announcementAndExam/v1/branchRanking/{yearMonth}")
    fun getBranchRanking(
        @Path("yearMonth") yearMonth: String
    ):Single<Response<GolfRankingInBranchResponse>>

    @GET("main/user/announcementAndExam/v1/participationRate/{yearMonth}")
    fun getSubjectRanking(
        @Path("yearMonth")  yearMonth: String
    ):Single<Response<GolfRankingSubjectResponse>>

    @GET("main/user/announcementAndExam/v1/allBranchRanking/{yearMonth}")
    fun getAllBranchRanking(
        @Path("yearMonth")  yearMonth: String
    ):Single<Response<GolfRankingAllBranchResponse>>

    @GET("trainingCenter/summary/weight")
    fun getTrainingManagementSummaryWeight(): Single<Response<TrainingManagementSummaryWeightResponse>>

    @GET("trainingCenter/summary/golfPower")
    fun getTrainingManagementSummaryGolfPower(): Single<Response<TrainingManagementSummaryGolfPowerResponse>>

    @GET("trainingCenter/summary/round")
    fun getTrainingManagementSummaryRound(): Single<Response<TrainingManagementSummaryRoundResponse>>

    @GET("trainingCenter/summary/round/{gmID}")
    fun getTrainingManagementSummaryRoundDetail(
        @Path("gmID") gmID: Int
    ): Single<Response<TrainingManagementSummaryRoundDetailResponse>>

    @GET("trainingCenter/summary/swingVideo")
    fun getTrainingManagementSummarySwingVideo(): Single<Response<TrainingManagementSummarySwingVideoResponse>>

    @POST("trainingCenter/summary/swingVideo/detail/update")
    fun updateTrainingManagementSummarySwingVideoDetail(
        @Body data: HashMap<String, TSSummarySwingVideoDetailUpdateReqModel>
    ): Single<Response<TrainingManagementSummarySwingVideoDetailUpdateResponse>>


    @GET("hgGame")
    fun getGameCenter(): Single<GameCenterResponse>

    @GET("hgGame/ranks")
    fun getGameRanks(): Single<GolfRankResponse>

    @GET("hgGame/individualGames")
    fun getGolfGames(): Single<GolfGameResponse>

    @DELETE("main/user/notifications/deleteAll")
    fun alramDeleteAll(): Completable

    @DELETE("main/user/notifications/delete/{notificationID}")
    fun alramDelete(
        @Path("notificationID") notifiCationId: Int
    ): Completable

    @Multipart
    @POST("main/user/uploadProfileImage")
    fun uploadProfileImage(
        @Part profileImage: MultipartBody.Part
    ): Single<ProfileImageResponse>

    @PUT("main/user/resetProfileImage")
    fun uploadDefaultProfileImage(): Completable

    @GET("main/user/pausedTickets")
    fun getPauseTickets(): Single<TicketManagerResponse>

    @GET("main/user/expiredTickets")
    fun getExpiredTickets(): Single<TicketManagerResponse>

    @GET("main/user/announcementAndBanners/v1/main")
    fun getBanners(): Single<Response<BannerDto>>

    @GET("main/user/banner/v1/main/record")
    fun getTraningBanners(): Single<Response<BannerDto>>

    @GET("main/user/announcementAndExam/v1/golfPowerExamStatus")
    fun getExamState(): Single<Response<ExamDto>>

    @POST("main/user/announcementAndExam/v1/joinMonthlyExamination")
    fun postJoinMonthlyExamination(
        @Body data: HashMap<String, ExamStateBody>
    ): Single<Response<ExamStateDto>>

    @GET("main/user/announcementAndExam/v1/myGolfPowerPageExamResultPageInfoDetail/{yearMonth}")
    fun getMyGolfPowerPageExamResultPageInfoDetail(
        @Path("yearMonth") yearMonth: String
    ): Single<Response<GolfPowerExamResultDto>>
}
