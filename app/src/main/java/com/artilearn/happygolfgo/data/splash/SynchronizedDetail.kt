package com.artilearn.happygolfgo.data.splash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SynchronizedDetail(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("profileImageURL")
    @Expose
    val profileImageURL: String?,

    @SerializedName("sex")
    @Expose
    val sex: Int,

    @SerializedName("nickname")
    @Expose
    val nickname: String?,

    @SerializedName("dateOfBirth")
    @Expose
    val dateOfBirth: String,

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: String,

    @SerializedName("branchID")
    @Expose
    val branchID: Int,

    @SerializedName("clientSecretKey")
    @Expose
    val clientSecretKey: String,

    @SerializedName("memo")
    @Expose
    val memo: String,

    @SerializedName("fcmToken")
    @Expose
    val fcmToken: String,

    @SerializedName("systemLanguage")
    @Expose
    val systemLanguage: Int,

    @SerializedName("accessToken")
    @Expose
    val accessToken: String,

    @SerializedName("checkinID")
    @Expose
    val checkinId: String
)