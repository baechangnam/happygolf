package com.artilearn.happygolfgo.data.lecture.source.remote

import com.artilearn.happygolfgo.api.LectureApiInterface
import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.data.lecture.LectureBookMarkResponse
import com.artilearn.happygolfgo.data.lecture.LectureCommentResponse
import com.artilearn.happygolfgo.data.lecture.LectureDetailResponse
import com.artilearn.happygolfgo.data.lecture.LectureHomeResponse
import com.artilearn.happygolfgo.data.lecture.LectureResponse
import io.reactivex.Single
import retrofit2.Response

class LectureRemoteDataSourceImpl(
    private val apiInterface: LectureApiInterface
) : LectureRemoteDataSource {


    override fun getLectureHomeList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureHomeResponse>> {
        return apiInterface.getLectureHomeList(keyword, sectNo,page)
    }

    override fun getLectureDetail(
        lectNo: String
    ): Single<Response<LectureDetailResponse>> {
        return apiInterface.getLectDetail(lectNo)
    }

    override fun addBookMark(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return apiInterface.addBookMark(lectNo)
    }

    override fun cancelBookMark(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return apiInterface.cancelBookMark(lectNo)
    }

    override fun addLike(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return apiInterface.addLike(lectNo)
    }
    override fun cancelLike(
        lectNo: String
    ): Single<Response<LectureBookMarkResponse>> {
        return apiInterface.cancelLike(lectNo)
    }

    override fun getLectureList(
        keyword: String,
        sectNo: String,
        page: String,
        order: String
    ): Single<Response<LectureResponse>> {
        return apiInterface.getLectureList(keyword, sectNo,page,order)
    }

    override fun getComment(
        lectNo : String
    ): Single<Response<List<LectureComment>>> {
        return apiInterface.getComment(lectNo)
    }

    override fun regComment(
        param : HashMap<String,String>
    ): Single<Response<LectureBookMarkResponse>> {
        return apiInterface.regComment(param)
    }


    override fun getLikeLectureList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureResponse>> {
        return apiInterface.getLikeLectureList(keyword,sectNo,page)
    }

    override fun getPickLectureList(
        keyword: String,
        sectNo: String,
        page: String
    ): Single<Response<LectureResponse>> {
        return apiInterface.getPickLectureList(keyword,sectNo,page)
    }


    override fun getCommentMy(
    ): Single<Response<LectureCommentResponse>> {
        return apiInterface.getCommentMy()
    }


    override fun lecturePlayEnd(
        lectNo: String,param : HashMap<String,String>

    ): Single<Response<LectureBookMarkResponse>> {
        return apiInterface.lecturePlayEnd(lectNo,param)
    }




}