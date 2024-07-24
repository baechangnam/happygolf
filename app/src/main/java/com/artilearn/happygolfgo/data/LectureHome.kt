package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.artilearn.happygolfgo.base.Identifiable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LectureHome(

    @SerializedName("sect_no")
    @Expose
    val sect_no: String,

    @SerializedName("sect_label")
    @Expose
    val sect_label: String,

    @SerializedName("sect_title")
    @Expose
    val sect_title: String,

    @SerializedName("first_reg_dtm")
    @Expose
    val first_reg_dtm: String,

    @SerializedName("total")
    @Expose
    val total: String,

    @SerializedName("lectures")
    @Expose
    val lectures: List<Lecture>


) : Parcelable, Identifiable {
    override val identifier: Any
        get() = sect_no

}

