package com.artilearn.happygolfgo.data.comment.source

import com.artilearn.happygolfgo.data.comment.CommentModel
import io.reactivex.Completable

interface CommentRepository {

    fun uploadLessonCommet(
        data: HashMap<String, CommentModel>
    ): Completable
}