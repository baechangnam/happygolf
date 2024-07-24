package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LogDetail(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("comment")
    @Expose
    val comment: String?,

    @SerializedName("videoURL")
    @Expose
    val videoURL: String?,

    @SerializedName("thumbnailURL")
    @Expose
    val thumbnailURL: String?,

    @SerializedName("ratings")
    @Expose
    val ratings: Float?,

    @SerializedName("isReviewed")
    @Expose
    val isReviewed: Boolean,

    @SerializedName("isConfirmed")
    @Expose
    val isConfirmed: Boolean,

    @SerializedName("confirmedDate")
    @Expose
    val confirmedDate: String?,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,

    @SerializedName("isReviewable")
    @Expose
    val isReviewable: Boolean,

    @SerializedName("employee")
    @Expose
    val employee: Employee
)

data class Employee(
    @SerializedName("name")
    @Expose
    val name: String
)