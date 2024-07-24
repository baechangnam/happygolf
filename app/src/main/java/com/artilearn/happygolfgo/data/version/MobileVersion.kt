package com.artilearn.happygolfgo.data.version

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MobileVersion(
    @SerializedName("android")
    @Expose
    val androidVersion: MobileVersionAndroid,
) : Parcelable