package com.artilearn.happygolfgo.ui.ticketmanager.model

import android.os.Parcelable
import com.artilearn.happygolfgo.base.Identifiable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpiredModel(
    val type: Int,
    val name: String,
    val date: String,
    val paid: String
) : Parcelable, Identifiable {
    override val identifier: Any
        get() = date
}