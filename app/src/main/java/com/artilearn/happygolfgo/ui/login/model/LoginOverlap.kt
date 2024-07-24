package com.artilearn.happygolfgo.ui.login.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginOverlap(
    val branchId: Int,
    val training: String
) : Parcelable