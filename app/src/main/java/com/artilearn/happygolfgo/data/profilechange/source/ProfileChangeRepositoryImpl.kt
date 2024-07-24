package com.artilearn.happygolfgo.data.profilechange.source

import com.artilearn.happygolfgo.data.profilechange.ProfileImageResponse
import com.artilearn.happygolfgo.data.profilechange.source.local.ProfileChangeLocalDataSource
import com.artilearn.happygolfgo.data.profilechange.source.remote.ProfileChangeRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

class ProfileChangeRepositoryImpl(
    private val localDataSource: ProfileChangeLocalDataSource,
    private val remoteDataSource: ProfileChangeRemoteDataSource
) : ProfileChangeRepository {
    override var profileImage: String?
        get() = localDataSource.profileImage
        set(value) {
            localDataSource.profileImage = value
        }

    override fun uploadProfileImage(image: MultipartBody.Part): Single<ProfileImageResponse> {
        return remoteDataSource.uploadProfileImage(image)
            .doOnSuccess { localDataSource.profileImage = it.data.profile }
    }

    override fun uploadDefaultProfileImage(): Completable {
        return remoteDataSource.uploadDefaultProfileImage()
            .doOnSubscribe { localDataSource.profileImage = null }
    }
}