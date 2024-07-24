package com.artilearn.happygolfgo.data.home.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.banner.BannerDto
import com.artilearn.happygolfgo.data.home.*
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSource
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class HomeRepositoryImpl(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val networkManager: NetworkManager
) : HomeRepository {

    override fun requestHomeTicket(): Single<Response<HomeResponse>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteHomeTicket()
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

    override fun requestAnnouncementAndExam(): Single<Response<AnnouncementAndExamResponse>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteAnnouncementAndExam()
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

    override fun requestJoinMonthlyExam(
        data:HashMap<String, JoinMonthlyExamInputModel>
    ): Single<Response<JoinMonthlyExaminationResponse>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteJoinMonthlyExam(data)
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

    override fun requestFreshFcmToken(data: HashMap<String, RefreshFcmTokenPutParam>): Single<Response<RefreshFcmTokenResponse>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteFreshFcmToken(data)
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

    override fun selectReservationList(): Single<Response<ReserveListResponse>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteSelectReservationList()
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

    override fun getBanner(): Single<Response<BannerDto>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteGetBanners()
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