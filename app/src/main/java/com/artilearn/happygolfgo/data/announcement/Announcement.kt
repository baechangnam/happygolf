package com.artilearn.happygolfgo.data.announcement

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Announcement(
    @SerializedName("issuedDate")
    @Expose
    val issuedDate: String = "",

    @SerializedName("subject")
    @Expose
    val subject: String = "...",

    @SerializedName("serviceUrl")
    @Expose
    val serviceUrl: String = "",

) : Parcelable