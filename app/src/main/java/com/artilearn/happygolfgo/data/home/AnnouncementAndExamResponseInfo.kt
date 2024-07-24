package com.artilearn.happygolfgo.data.home

import com.artilearn.happygolfgo.data.announcement.Announcement
import com.artilearn.happygolfgo.data.examination.Examination
import com.artilearn.happygolfgo.data.version.MobileVersion
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnnouncementAndExamResponseInfo(
    @SerializedName("announcement")
    @Expose
    val announcement: Announcement,

    @SerializedName("exam")
    @Expose
    val exam: Examination,

    @SerializedName("version")
    @Expose
    val version: MobileVersion
)

data class AnnouncementAndExamResponseData(
    @SerializedName("errorCode")
    @Expose
    val errorCode: Int,

    @SerializedName("errorMessage")
    @Expose
    val errorMessage: String,

    @SerializedName("res")
    @Expose
    val res:AnnouncementAndExamResponseInfo,
)