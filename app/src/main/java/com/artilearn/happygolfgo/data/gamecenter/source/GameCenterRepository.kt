package com.artilearn.happygolfgo.data.gamecenter.source

import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.exam.ExamDto
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPowerExamResultDto
import com.artilearn.happygolfgo.data.exam.state.ExamStateDto
import com.artilearn.happygolfgo.data.exam.state.request.ExamStateBody
import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import io.reactivex.Single
import retrofit2.Response

interface GameCenterRepository {
    fun getGames(): Single<GameCenterResponse>
    //comments: vc45 API 추가 할때
    fun getTrainingManagementSummaryTime(): Single<Response<TrainingManagementSummaryTimeResponse>>
    fun getTrainingManagementSummaryWeight(): Single<Response<TrainingManagementSummaryWeightResponse>>
    fun getTrainingManagementSummaryGolfPower(): Single<Response<TrainingManagementSummaryGolfPowerResponse>>
    fun getTrainingManagementSummaryRound(): Single<Response<TrainingManagementSummaryRoundResponse>>
    fun getTrainingManagementSummarySwingVideo(): Single<Response<TrainingManagementSummarySwingVideoResponse>>
    fun getTrainingManagementSummaryRoundDetail(gmID: Int): Single<Response<TrainingManagementSummaryRoundDetailResponse>>
    fun updateTrainingManagementSummarySwingVideoDetail(
        data: HashMap<String, TSSummarySwingVideoDetailUpdateReqModel>
    ): Single<Response<TrainingManagementSummarySwingVideoDetailUpdateResponse>>
    fun getMyGolfPowerExamResultPageInfo() : Single<Response<MyGolfPowerExamResultPageInfoResponse>>
    fun getMyGolfPowerPageExamResultPageInfoDetail(pageIndex:Int) : Single<Response<MyGolfPowerExamResultPageInfoDetailResponse>>
    fun getBranchRanking(yearMonth:String) : Single<Response<GolfRankingInBranchResponse>>
    fun getSubjectRanking(yearMonth:String) : Single<Response<GolfRankingSubjectResponse>>
    fun getAllBranchRanking(yearMonth:String) : Single<Response<GolfRankingAllBranchResponse>>
    fun getTraningBanners() : Single<Response<BannerDto>>
    fun getExamState() : Single<Response<ExamDto>>
    fun postJoinMonthlyExamination(data: HashMap<String, ExamStateBody>) : Single<Response<ExamStateDto>>
    fun getMyGolfPowerPageExamResultPageInfoDetail(yearMonth: String) : Single<Response<GolfPowerExamResultDto>>
}