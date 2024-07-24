package com.artilearn.happygolfgo.data.gamecenter.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.exam.ExamDto
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPowerExamResultDto
import com.artilearn.happygolfgo.data.exam.state.ExamStateDto
import com.artilearn.happygolfgo.data.exam.state.request.ExamStateBody
import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import io.reactivex.Single
import retrofit2.Response

class GameCenterRemoteDataSourceImpl(
    private val api: ApiInterface
) : GameCenterRemoteDataSource {
    override fun getGames(): Single<GameCenterResponse> {
        return api.getGameCenter()
    }
    //comments:  vc45 API  추가
    override fun getTrainingManagementSummaryTime(): Single<Response<TrainingManagementSummaryTimeResponse>> {
        return api.getTrainingManagementSummaryTime()
    }

    override fun getTrainingManagementSummaryWeight(): Single<Response<TrainingManagementSummaryWeightResponse>> {
        return api.getTrainingManagementSummaryWeight()
    }

    override fun getTrainingManagementSummaryGolfPower(): Single<Response<TrainingManagementSummaryGolfPowerResponse>> {
        return api.getTrainingManagementSummaryGolfPower()
    }

    override fun getTrainingManagementSummaryRound(): Single<Response<TrainingManagementSummaryRoundResponse>> {
        return api.getTrainingManagementSummaryRound()
    }

    override fun getTrainingManagementSummarySwingVideo(): Single<Response<TrainingManagementSummarySwingVideoResponse>> {
        return api.getTrainingManagementSummarySwingVideo()
    }

    override fun getTrainingManagementSummaryRoundDetail(gmID: Int): Single<Response<TrainingManagementSummaryRoundDetailResponse>> {
        return api.getTrainingManagementSummaryRoundDetail(gmID)
    }

    override fun updateTrainingManagementSummarySwingVideoDetail(data: HashMap<String, TSSummarySwingVideoDetailUpdateReqModel>): Single<Response<TrainingManagementSummarySwingVideoDetailUpdateResponse>> {
        return api.updateTrainingManagementSummarySwingVideoDetail(data)
    }

    override fun getMyGolfPowerExamResultPageInfo(): Single<Response<MyGolfPowerExamResultPageInfoResponse>> {
        return api.getMyGolfPowerExamResultPageInfo()
    }

    override fun getMyGolfPowerPageExamResultPageInfoDetail(pageIndex:Int) : Single<Response<MyGolfPowerExamResultPageInfoDetailResponse>> {
        return api.getMyGolfPowerPageExamResultPageInfoDetail(pageIndex)
    }

    override fun getMyGolfPowerPageExamResultPageInfoDetail(yearMonth: String): Single<Response<GolfPowerExamResultDto>> {
        return api.getMyGolfPowerPageExamResultPageInfoDetail(yearMonth)
    }

    override fun getBranchRanking(yearMonth: String): Single<Response<GolfRankingInBranchResponse>> {
        return api.getBranchRanking(yearMonth)
    }

    override fun getSubjectRanking(yearMonth: String): Single<Response<GolfRankingSubjectResponse>> {
        return api.getSubjectRanking(yearMonth)
    }

    override fun getAllBranchRanking(yearMonth: String): Single<Response<GolfRankingAllBranchResponse>> {
        return api.getAllBranchRanking(yearMonth)
    }

    override fun getTraningBanners(): Single<Response<BannerDto>> {
        return api.getTraningBanners()
    }

    override fun getExamState(): Single<Response<ExamDto>> {
        return api.getExamState()
    }

    override fun postJoinMonthlyExamination(data: HashMap<String, ExamStateBody>): Single<Response<ExamStateDto>> {
        return api.postJoinMonthlyExamination(data)
    }
}