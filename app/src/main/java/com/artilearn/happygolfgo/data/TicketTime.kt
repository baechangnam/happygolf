package com.artilearn.happygolfgo.data

import android.os.Parcelable
import android.view.View
import androidx.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketTime(
    @SerializedName("plateAvailabilityID")
    @Expose
    val plateAvailabilityId: Int?,

    @SerializedName("lessonAvailabilityID")
    @Expose
    val lessonAvailabilityId: Int?,

    @SerializedName("status")
    @Expose
    val status: Int?,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int?,

    @SerializedName("availableCount")
    @Expose
    val availableCount: Int?,
    //feature/2022/0216/multiSelection
    //다중 선택
    var isChecked:Boolean = false,
    var isMutiple:Boolean = false,    //다중 선택인지 여부

    var firstStartDate:String? = "", //
    var secondStartDate:String? = "",
    var thirdStartDate:String? = "",

    var firstEndDate:String? = "", //
    var secondEndDate:String? = "",
    var thirdEndDate:String? = "",

    var firstPlateAvailabilityId:Int = 0,
    var secondPlateAvailabilityId:Int = 0,
    var thirdPlateAvailabilityId:Int = 0,
) : Parcelable