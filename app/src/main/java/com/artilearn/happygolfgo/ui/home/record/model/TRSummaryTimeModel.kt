package com.artilearn.happygolfgo.ui.home.record.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class TRSummaryTimeModel(
    val title: String,
    val time: String,
    val unit: String
)

data class TRSummaryWeightModel(
    val title:String,
    val subTitle:String,
    val drange:Float,
    val appr: Float,
    val putt: Float,
    val gpower:Float,
    val stroke:Float,
)

data class TRSummaryGolfPowerModel(
    val title:String,
    val subTitle:String,
    val labelBest:String,
    val labelAvg: String,
)

data class TRSummaryGolfPowerRecordModel(
    val type: String,
    val title: String,
    val best: Float,
    val avg: Float,
)

data class TRSummaryRoundModel(
    val title: String,
    val subTitle: String,
    val isNoData: Int,
    val bestTitle:String,
    val bestCC:String,
    val bestDate:String,
    val bestScore:String,
    val bestGmId: Int,
    val avgTitle: String,
    val avgCC:String,
    val avgDate:String,
    val avgScore: String,
    val lastTitle: String,
    val lastCC: String,
    val lastDate:String,
    val lastScore: String,
    val lastGmId:Int,
)

data class TRSummaryRoundRecordModel(
    val  gmId: Int,
    val  cc: String,
    val  regDate: String,
    val  score: Int,
)

@Parcelize
data class TRSummarySwingVideoRecordModel(
    var id:  Int,
    val club: String,
    val dist: String,
    val carry: String,
    val speed: String,
    val angle: String,
    val backspin: String,
    val shotType: String,
    val shotImageIndex:String,
    val unit:String,
    val shortUnit:String,
    val svrUrl1: String,
    val svrUrl2: String,
    val regDate: String,
    val svrUrlThumbNail1:String,
    val svrUrlThumbNail2:String,
    val videoPosition:String,
) : Parcelable

data class TRSummaryRoundDetailModel (
    val title: String?,
    val subTitle: String?,
    val gmID: String?,
    val ccName: String?,
    val score: String?,
    val roundDate: String?,
    val avgDriverDist: String?,
    val avgDriverDistTitle: String?,
    val avgDriverDistUnit: String?,
    val inCourseScore: String?,
    val outCourseScore: String?,
    val longestShot: String?,
    val longestShotTitle: String?,
    val longestShotUnit: String?,
    val onFairwayRate: String?,
    val onFairwayRateTitle: String?,
    val onFairwayRateUnit: String?,
    val onGreenRate: String?,
    val onGreenRateTitle: String?,
    val onGreenRateUnit: String?,
    val avgPutt: String?,
    val avgPuttTitle: String?,
    val avgPuttUnit: String?,
    val parSave: String?,
    val parSaveTitle: String?,
    val parSaveUnit: String?,
    val hole1:String = "0",
    val hole2:String = "0",
    val hole3:String = "0",
    val hole4:String = "0",
    val hole5:String = "0",
    val hole6:String = "0",
    val hole7:String = "0",
    val hole8:String = "0",
    val hole9:String = "0",
    val hole10:String = "0",
    val hole11:String = "0",
    val hole12:String = "0",
    val hole13:String = "0",
    val hole14:String = "0",
    val hole15:String = "0",
    val hole16:String = "0",
    val hole17:String = "0",
    val hole18:String = "0",
)

data class TRSummaryRoundDetailRecordModel (
    val holeIndex: String,
    val par: String,
    val score: String,
    val deltaScore: String,
)

data class TSSummarySwingVideoDetailUpdateReqModel (
    var id:  Int,
    var type:Int, //1 or 2
)

data class  TRMyGolfPowerExamResultPageInfoModel  (
     var pageCount: Int,
     var currentPageIndex : Int
)
