package com.artilearn.happygolfgo.ui.ticketmanager.model

import android.os.Parcelable
import com.artilearn.happygolfgo.base.Identifiable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PauseModel(
    val name: String,
    val type: Int,
    val date: String,
    val pauseList: List<String>
) : Parcelable, Identifiable {
    override val identifier: Any
        get() = date
}