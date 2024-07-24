package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LessonLog(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,

    @SerializedName("employeeName")
    @Expose
    val employeeName: String
) : Parcelable