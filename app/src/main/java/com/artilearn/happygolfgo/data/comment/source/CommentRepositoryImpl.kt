package com.artilearn.happygolfgo.data.comment.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.source.remote.CommentRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Completable

class CommentRepositoryImpl(
    private val commentRemoteDataSource: CommentRemoteDataSource,
    private val networkManager: NetworkManager
) : CommentRepository {

    override fun uploadLessonCommet(
        data: HashMap<String, CommentModel>
    ): Completable {
        return if (networkManager.networkState()) {
            commentRemoteDataSource.remoteUploadComment(data)
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }
}