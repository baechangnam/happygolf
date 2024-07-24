package com.artilearn.happygolfgo.data.exam

import com.google.gson.annotations.SerializedName

data class Exam(
    @SerializedName("contents") val contents: String,
    @SerializedName("headTitle") val headTitle: String,
    @SerializedName("state") val state: ExamState,
    @SerializedName("subject") val subject: String,
    @SerializedName("totalParticipants") val totalParticipants: String,
    @SerializedName("currentYearMonth") val currentYearMonth: String,
    @SerializedName("updateDate") val updateDate: String
) {
    enum class ExamState{
        waiting, // 시험시간이 정해지지 않아 대기중인 상태,
        taking, // 응시중
        done, // 응시완료. 시험기간이 끝나거나, 혹은 시험을 모두 마쳤을 경우
        not, // 시험에 참여하지 않음
        open // 시험기간이 되어 응시할 수 있는 상태
    }
}