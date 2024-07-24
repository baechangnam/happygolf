package com.artilearn.happygolfgo.data.myinfo.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.User
import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoResponse
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSource
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class MyInfoRepositoryImpl(
    private val myinfoLocalDataSource: MyInfoLocalDataSource,
    private val myInfoRemoteDataSource: MyInfoRemoteDataSource,
    private val networkManager: NetworkManager
) : MyInfoRepository {

    override val nickname: String?
        get() = myinfoLocalDataSource.nickname

    override val name: String
        get() = myinfoLocalDataSource.name

    override fun uploadProfile(
        data: HashMap<String, MyInfoModel>
    ): Single<MyInfoResponse> {
        return if (networkManager.networkState()) {
            myInfoRemoteDataSource.remoteUploadProfile(data)
                .flatMap {
                    saveUserInfo(it.data)
                    Single.just(it)
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    private fun saveUserInfo(user: User) {
        with(myinfoLocalDataSource) {
            name = user.name
            profileImageURL = user.profileImageURL
            nickname = user.nickname
            clientSecretKey = user.clientSecretKey
            fcmToken = user.fcmToken
            accessToken = user.accessToken
        }
    }
}