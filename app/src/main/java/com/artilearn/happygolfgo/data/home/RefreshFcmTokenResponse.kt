package com.artilearn.happygolfgo.data.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshFcmTokenPutParam (
    val operatingSystem:String,
    val fcmToken:String,
)

data class RefreshFcmTokenResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,
)
/*{
    data class RefreshFcmTokenRes(
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
    data class RefreshFcmTokenResponseData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res:RefreshFcmTokenRes,
    )
}
*/