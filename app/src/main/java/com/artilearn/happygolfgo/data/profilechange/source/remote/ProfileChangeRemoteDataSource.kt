package com.artilearn.happygolfgo.data.profilechange.source.remote

import com.artilearn.happygolfgo.data.profilechange.ProfileImageResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

interface ProfileChangeRemoteDataSource {
    fun uploadProfileImage(image: MultipartBody.Part): Single<ProfileImageResponse>
    fun uploadDefaultProfileImage(): Completable
}