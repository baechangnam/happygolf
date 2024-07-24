package com.artilearn.happygolfgo.data.gamecenter.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.exam.ExamDto
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPowerExamResultDto
import com.artilearn.happygolfgo.data.exam.state.ExamStateDto
import com.artilearn.happygolfgo.data.exam.state.request.ExamStateBody
import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.data.gamecenter.source.local.GameCenterLocalDataSource
import com.artilearn.happygolfgo.data.gamecenter.source.remote.GameCenterRemoteDataSource
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class GameCenterRepositoryImpl(
    private val remote: GameCenterRemoteDataSource,
    private val local: GameCenterLocalDataSource,
    private val network: NetworkManager
) : GameCenterRepository {
    override fun getGames(): Single<GameCenterResponse> {
        return if (network.networkState()) {
            remote.getGames()
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override  fun getTrainingManagementSummaryTime(): Single<Response<TrainingManagementSummaryTimeResponse>> {
        return if (network.networkState()) {
            remote.getTrainingManagementSummaryTime()
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

    override fun getTrainingManagementSummaryWeight(): Single<Response<TrainingManagementSummaryWeightResponse>> {
        return if (network.networkState()) {
            remote.getTrainingManagementSummaryWeight()
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getTrainingManagementSummaryGolfPower(): Single<Response<TrainingManagementSummaryGolfPowerResponse>> {
        return if (network.networkState()) {
            remote.getTrainingManagementSummaryGolfPower()
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getTrainingManagementSummaryRound(): Single<Response<TrainingManagementSummaryRoundResponse>> {
        return if (network.networkState()) {
            remote.getTrainingManagementSummaryRound()
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getTrainingManagementSummarySwingVideo(): Single<Response<TrainingManagementSummarySwingVideoResponse>> {
        return if (network.networkState()) {
            remote.getTrainingManagementSummarySwingVideo()
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

    override fun getTrainingManagementSummaryRoundDetail(gmID: Int): Single<Response<TrainingManagementSummaryRoundDetailResponse>> {
        return if (network.networkState()) {
            remote.getTrainingManagementSummaryRoundDetail(gmID)
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

    override fun updateTrainingManagementSummarySwingVideoDetail(data: HashMap<String, TSSummarySwingVideoDetailUpdateReqModel>): Single<Response<TrainingManagementSummarySwingVideoDetailUpdateResponse>> {
        return if (network.networkState()) {
            remote.updateTrainingManagementSummarySwingVideoDetail(data)
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

    override fun getMyGolfPowerExamResultPageInfo(): Single<Response<MyGolfPowerExamResultPageInfoResponse>> {
        return if (network.networkState()) {
            remote.getMyGolfPowerExamResultPageInfo()
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

    override fun getMyGolfPowerPageExamResultPageInfoDetail(pageIndex:Int):
            Single<Response<MyGolfPowerExamResultPageInfoDetailResponse>> {
        return if (network.networkState()) {
            remote.getMyGolfPowerPageExamResultPageInfoDetail(pageIndex)
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

    override fun getMyGolfPowerPageExamResultPageInfoDetail(yearMonth: String): Single<Response<GolfPowerExamResultDto>> {
        return if (network.networkState()) {
            remote.getMyGolfPowerPageExamResultPageInfoDetail(yearMonth)
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

    override fun getBranchRanking(yearMonth: String): Single<Response<GolfRankingInBranchResponse>> {
        return if (network.networkState()) {
            remote.getBranchRanking(yearMonth)
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

    override fun getSubjectRanking(yearMonth: String): Single<Response<GolfRankingSubjectResponse>> {
        return if (network.networkState()) {
            remote.getSubjectRanking(yearMonth)
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
    override fun getAllBranchRanking(yearMonth: String): Single<Response<GolfRankingAllBranchResponse>> {
        return if (network.networkState()) {
            remote.getAllBranchRanking(yearMonth)
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

    override fun getTraningBanners(): Single<Response<BannerDto>> {
        return if (network.networkState()) {
            remote.getTraningBanners()
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

    override fun getExamState(): Single<Response<ExamDto>> {
        return if (network.networkState()) {
            remote.getExamState()
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

    override fun postJoinMonthlyExamination(data: HashMap<String, ExamStateBody>): Single<Response<ExamStateDto>> {
        return if (network.networkState()) {
            remote.postJoinMonthlyExamination(data)
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