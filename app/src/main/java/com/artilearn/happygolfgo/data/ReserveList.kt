package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReserveList(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("status")
    @Expose
    val status: Int,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean,

    @SerializedName("isCancellable")
    @Expose
    val isCancellable: Boolean,

    @SerializedName("plateNumber")
    @Expose
    val plateNumber: Int?,

    @SerializedName("ticket")
    @Expose
    val ticket: ReservationTicket?,

    @SerializedName("multipleBranchTicketUser")
    @Expose
    var multipleBranchTicketUser:Boolean?,

    @SerializedName("branchName")
    @Expose
    val branchName: String?,
) : Parcelable {

    @Parcelize
    data class ReservationTicket(
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("type")
        val type: Int,
        @field:SerializedName("period")
        val period: Int,
        @field:SerializedName("totalCount")
        val totalCount: Int,
        @field:SerializedName("usedCount")
        val usedCount: Int,
        @field:SerializedName("remainingCount")
        val remainingCount: Int,
        @field:SerializedName("isUnlimited")
        val isUnlimited: Boolean,
        @field:SerializedName("onlyOnceToday")
        val onlyOnceToday: Boolean,
        @field:SerializedName("startDate")
        val startDate: String,
        @field:SerializedName("endDate")
        val endDate: String
    ) : Parcelable

}