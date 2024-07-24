package com.artilearn.happygolfgo.data.examination

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Examination(
    @SerializedName("subject")
    @Expose
    val subject: String = "",

    @SerializedName("contents")
    @Expose
    val contents: String = "",

    @SerializedName("golfPowerExamCategoryID")
    @Expose
    val golfPowerExamCategoryID: String = "",

    @SerializedName("golfPowerMonthlyExamID")
    @Expose
    var golfPowerMonthlyExamID: String = "",

    @SerializedName("state")
    @Expose
    var state: String = "",

    @SerializedName("retryCount")
    @Expose
    val retryCount: String = "",

    @SerializedName("putting")
    @Expose
    var putting: String = "",

    @SerializedName("sg")
    @Expose
    var sg: String = "",

    @SerializedName("irn")
    @Expose
    var irn: String = "",

    @SerializedName("wu")
    @Expose
    var wu: String = "",

    @SerializedName("drv")
    @Expose
    var drv: String = "",

    var isWaiting:Boolean = true,
    var stateNumber:Int = 1,
    var actionTitle :String = "",
    var actionButtonOn:Boolean = false,

    ) : Parcelable