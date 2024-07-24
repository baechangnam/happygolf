package com.artilearn.happygolfgo.data.ticketmanager

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketModelItem(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("period")
    @Expose
    val period: Int,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("usedCount")
    @Expose
    val usedCount: Int,

    @SerializedName("onlyOnceToday")
    @Expose
    val onlyOnceToday: Boolean,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("allowToBook")
    @Expose
    val allowToBook: Boolean?,

    @SerializedName("pausesPeriods")
    @Expose
    val pasuePeriods: List<TicketPausePeriodsModel>,

    @SerializedName("totalPrice")
    @Expose
    val totalPrice: Int
) : Parcelable

@Parcelize
data class TicketPausePeriodsModel(
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