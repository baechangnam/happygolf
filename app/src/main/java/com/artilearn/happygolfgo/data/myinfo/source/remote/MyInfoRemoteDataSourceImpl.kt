package com.artilearn.happygolfgo.data.myinfo.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoResponse
import io.reactivex.Single

class MyInfoRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : MyInfoRemoteDataSource {

    override fun remoteUploadProfile(
        data: HashMap<String, MyInfoModel>
    ): Single<MyInfoResponse> {
        return apiInterface.updateProfile(data)
    }
}