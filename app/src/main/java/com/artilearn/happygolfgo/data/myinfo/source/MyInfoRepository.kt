package com.artilearn.happygolfgo.data.myinfo.source

import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoResponse
import io.reactivex.Single

interface MyInfoRepository {

    val nickname: String?
    val name: String

    fun uploadProfile(
        data: HashMap<String, MyInfoModel>
    ): Single<MyInfoResponse>
}