package com.artilearn.happygolfgo.data.comment.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.comment.CommentModel
import io.reactivex.Completable

class CommentRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : CommentRemoteDataSource {

    override fun remoteUploadComment(
        data: HashMap<String, CommentModel>
    ): Completable {
        return apiInterface.uploadLessonComment(data)
    }
}