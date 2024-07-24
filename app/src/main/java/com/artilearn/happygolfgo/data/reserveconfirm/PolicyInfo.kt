package com.artilearn.happygolfgo.data.reserveconfirm

import com.artilearn.happygolfgo.data.Policy
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PolicyInfo(
    @SerializedName("policy")
    @Expose
    val policy: Policy
)