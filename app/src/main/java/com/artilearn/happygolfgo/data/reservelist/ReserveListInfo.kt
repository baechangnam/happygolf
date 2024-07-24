package com.artilearn.happygolfgo.data.reservelist

import com.artilearn.happygolfgo.data.ReserveList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReserveListInfo(
    @SerializedName("plateReservations")
    @Expose
    val plateReservations: ArrayList<ReserveList>,

    @SerializedName("lessonReservations")
    @Expose
    val lessonReservations: ArrayList<ReserveList>
)