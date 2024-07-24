package com.artilearn.happygolfgo.data.exam.golfpower

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("avg") val avg: Avg,
    @SerializedName("drv") val drv: GolfPower,
    @SerializedName("iron") val iron: GolfPower,
    @SerializedName("putt") val putt: GolfPower,
    @SerializedName("sg") val sg: GolfPower,
    @SerializedName("wu") val wu: GolfPower,
    @SerializedName("totalParticipants") val totalParticipants: String,
    @SerializedName("yearMonth") val yearMonth: String,
    @SerializedName("improvement") val improvement: Improvement,
    @SerializedName("updatedDate") val updatedDate: String
)