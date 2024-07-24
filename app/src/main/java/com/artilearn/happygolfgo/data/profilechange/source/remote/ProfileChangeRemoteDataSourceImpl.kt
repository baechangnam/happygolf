package com.artilearn.happygolfgo.data.profilechange.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.profilechange.ProfileImageResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

class ProfileChangeRemoteDataSourceImpl(private val api: ApiInterface) : ProfileChangeRemoteDataSource {
    override fun uploadProfileImage(image: MultipartBody.Part): Single<ProfileImageResponse> {
        return api.uploadProfileImage(image)
    }

    override fun uploadDefaultProfileImage(): Completable {
        return api.uploadDefaultProfileImage()
    }
}