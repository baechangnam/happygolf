package com.artilearn.happygolfgo.data.comment.source.remote

import com.artilearn.happygolfgo.data.comment.CommentModel
import io.reactivex.Completable

interface CommentRemoteDataSource {

    fun remoteUploadComment(
        data: HashMap<String, CommentModel>
    ): Completable
}