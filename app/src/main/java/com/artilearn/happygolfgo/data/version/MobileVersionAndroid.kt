package com.artilearn.happygolfgo.data.version

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MobileVersionAndroid(
    @SerializedName("ver")
    @Expose
    val ver: String?,

    @SerializedName("forceUpdate")
    @Expose
    val forceUpdate: String?,

    @SerializedName("updateUrl")
    @Expose
    val updateUrl: String?,
) : Parcelable