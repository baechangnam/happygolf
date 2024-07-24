package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.artilearn.happygolfgo.base.Identifiable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LectureComment(


    val level: String,
    val cmmt_no: String,
    val uppr_cmmt_no: String,
    val reg_usr_id: String,
    val first_reg_dtm: String,
    val cmmt_text: String,
    val reg_dtm: String,




) : Parcelable, Identifiable {
    override val identifier: Any
        get() = cmmt_no
}