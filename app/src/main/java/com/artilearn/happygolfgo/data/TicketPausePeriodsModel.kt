package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TicketPausePeriodsModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("ticketID")
    @Expose
    val ticketId: Int,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String
) : Parcelable