package com.artilearn.happygolfgo.data.myinfo.source.remote

import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoResponse
import io.reactivex.Single

interface MyInfoRemoteDataSource {
    
    fun remoteUploadProfile(
        data: HashMap<String, MyInfoModel>
    ): Single<MyInfoResponse>
}