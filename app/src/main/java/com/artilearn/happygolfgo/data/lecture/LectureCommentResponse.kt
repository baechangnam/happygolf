package com.artilearn.happygolfgo.data.lecture

import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.data.LectureHome
import com.artilearn.happygolfgo.databinding.ItemHomeLectureListBinding
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LectureCommentResponse(

    @SerializedName("comments")
    @Expose
    val comments : List<LectureComment>

)