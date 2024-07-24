package com.artilearn.happygolfgo.data.profilechange.source

import com.artilearn.happygolfgo.data.profilechange.ProfileImageResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

interface ProfileChangeRepository {
    var profileImage: String?

    fun uploadProfileImage(image: MultipartBody.Part): Single<ProfileImageResponse>

    fun uploadDefaultProfileImage(): Completable
}