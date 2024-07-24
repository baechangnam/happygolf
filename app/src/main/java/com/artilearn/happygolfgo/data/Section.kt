package com.artilearn.happygolfgo.data

import android.os.Parcel
import android.os.Parcelable
import com.artilearn.happygolfgo.base.Identifiable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Section(

    @SerializedName("sect_no")
    @Expose
    val sect_no: String,

    @SerializedName("sect_label")
    @Expose
    val sect_label: String,

    @SerializedName("sect_title")
    @Expose
    val sect_title: String,




) : Parcelable, Identifiable {
    override val identifier: Any
        get() = sect_no

}