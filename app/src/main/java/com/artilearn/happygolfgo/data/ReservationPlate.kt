package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReservationPlate(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("status")
    @Expose
    var status: Int,

    @SerializedName("startDate")
    @Expose
    var startDate: String,

    @SerializedName("endDate")
    @Expose
    var endDate: String,

    @SerializedName("secondStartDate")
    @Expose
    var secondStartDate: String?,

    @SerializedName("secondEndDate")
    @Expose
    var secondEndDate: String?,

    @SerializedName("thirdStartDate")
    @Expose
    var thirdStartDate: String?,

    @SerializedName("thirdEndDate")
    @Expose
    var thirdEndDate: String?,

    @SerializedName("plateNumber")
    @Expose
    var plateNumber: String?,

    @SerializedName("remainingCount")
    @Expose
    var remainingCount: Int,

    @SerializedName("totalCount")
    @Expose
    var totalCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    var isUnlimited: Boolean
) : Parcelable