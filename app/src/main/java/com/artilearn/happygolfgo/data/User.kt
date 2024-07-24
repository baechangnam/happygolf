package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("profileImageURL")
    @Expose
    val profileImageURL: String,

    @SerializedName("operatingSystem")
    @Expose
    val operatingSystem: Int,

    @SerializedName("sex")
    @Expose
    val sex: Int,

    @SerializedName("nickname")
    @Expose
    val nickname: String,

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

    @SerializedName("fcmToken")
    @Expose
    val fcmToken: String,

    @SerializedName("isSigedIn")
    @Expose
    val isSigedIn: Boolean,

    @SerializedName("systemLanguage")
    @Expose
    val systemLanguage: Int,

    @SerializedName("memo")
    @Expose
    val memo: String,

    @SerializedName("accessToken")
    @Expose
    val accessToken: String,
    //DONETODO: checkinID == null  Version 41(2.2.5)
    @SerializedName("checkinID")
    @Expose
    val checkinId: String?,

    @SerializedName("branches")
    @Expose
    val overlapUser: List<OverlapUser>? = null
)

data class OverlapUser(
    @SerializedName("branchID")
    @Expose
    val branchID: Int,

    @SerializedName("name")
    @Expose
    val name: String
)