package com.artilearn.happygolfgo.ui.home.record.mapper

import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.ui.home.record.model.*
import java.text.SimpleDateFormat
import java.util.*

//2023.04.05 remove summary time in training record section
object TRSummaryTimeMapper
    : TrainingRecordMapper<TrainingManagementSummaryTimeResponse.TrainingManagementSummaryTimeRes,
        TRSummaryTimeModel> {
    override fun mapper(from: TrainingManagementSummaryTimeResponse.TrainingManagementSummaryTimeRes): TRSummaryTimeModel {
        return TRSummaryTimeModel(
            title = from.title ?: "",
            time = from.time ?: "",
            unit = from.unit ?: ""
        )
    }
}

object TRSummaryWeightMapper
    : TrainingRecordMapper<TrainingManagementSummaryWeightResponse.TrainingManagementSummaryWeightRes,
        TRSummaryWeightModel> {
    override fun mapper(from: TrainingManagementSummaryWeightResponse.TrainingManagementSummaryWeightRes): TRSummaryWeightModel {
        return TRSummaryWeightModel(
            title = from.title ?: "",
            subTitle = from.subTitle ?: "",
            drange = (from.drange ?: 0.0) as Float,
            appr = (from.appr ?: 0.0) as Float,
            putt = (from.putt ?: 0.0) as Float,
            gpower = (from.gpower ?: 0.0) as Float,
            stroke = (from.stroke ?: 0.0) as Float
        )
    }
}

object TRSummaryGolfPowerMapper
    : TrainingRecordMapper<TrainingManagementSummaryGolfPowerResponse.TrainingManagementSummaryGolfPowerRes,
        TRSummaryGolfPowerModel> {
    override fun mapper(from: TrainingManagementSummaryGolfPowerResponse.TrainingManagementSummaryGolfPowerRes): TRSummaryGolfPowerModel {
        return TRSummaryGolfPowerModel(
            title = from.title ?: "",
            subTitle = from.subTitle ?: "",
            labelBest = from.labelBest ?: "" ,
            labelAvg = from.labelAvg ?: "" ,
        )
    }
}

object TrainingManagementGolfPowerRecordsMapper
    : TrainingRecordMapper<TrainingManagementSummaryGolfPowerResponse.TrainingManagementSummaryGolfPowerRes,
        List<TRSummaryGolfPowerRecordModel>> {
    override fun mapper(from: TrainingManagementSummaryGolfPowerResponse.TrainingManagementSummaryGolfPowerRes): List<TRSummaryGolfPowerRecordModel> {
        return from.records.map { record ->
            TRSummaryGolfPowerRecordModel(
            type =  record.type ?: "",
            title = record.title ?: "",
            best =  (record.best ?: 0.0) as Float,
            avg =  (record.avg ?: 0.0) as Float,
            )
        }
    }
}

object TRSummaryRoundMapper
    : TrainingRecordMapper<TrainingManagementSummaryRoundResponse.TrainingManagementSummaryRoundRes,
        TRSummaryRoundModel> {
    override fun mapper(from: TrainingManagementSummaryRoundResponse.TrainingManagementSummaryRoundRes): TRSummaryRoundModel {
             if (from.isNoData == null || from.isNoData == 1) {
                 return  TRSummaryRoundModel(
                     title = from.title ?: "스크린 라운딩 기록",
                     subTitle = from .subTitle ?: "(최근 3개월)",
                     isNoData = from.isNoData ?: -1,
                     bestTitle = from.bestTitle ?: "",
                     bestCC = from.bestCC ?: "",
                     bestDate = from.bestDate ?: "",
                     bestScore = from.bestScore ?: "",
                     bestGmId = from.bestGmId ?: -1,
                     avgTitle = from.avgTitle ?: "",
                     avgCC = from.avgCC ?: "",
                     avgDate = from.avgDate ?: "",
                     avgScore = from.avgScore ?: "",
                     lastTitle = from.lastTitle ?: "",
                     lastCC = from.lastCC ?: "",
                     lastDate = from.lastDate ?: "",
                     lastScore = from.lastScore ?: "",
                     lastGmId = from.lastGmId ?: -1
                 )
             } else {
                 val ret = TRSummaryRoundModel(
                     title = from.title ?: "스크린 라운딩 기록",
                     subTitle = from .subTitle ?: "(최근 3개월)",
                     isNoData = from.isNoData ?: -1,
                     bestTitle = from.bestTitle ?: "",
                     bestCC = from.bestCC ?: "",
                     bestDate = convertedDate(from.bestDate ?: "0000.00.00"),
                     bestScore = from.bestScore ?: "",
                     bestGmId = from.bestGmId ?: -1,
                     avgTitle = from.avgTitle ?: "",
                     avgCC = from.avgCC ?: "",
                     avgDate = from.avgDate ?: "",
                     avgScore = from.avgScore ?: "",
                     lastTitle = from.lastTitle ?: "",
                     lastCC = from.lastCC ?: "",
                     lastDate = convertedDate(from.lastDate ?: "0000.00.00"),
                     lastScore = from.lastScore ?: "",
                     lastGmId = from.lastGmId ?: -1
                 )
                 return ret;
             }
    }

    private  fun convertedDate(time:String) : String {
         try {
                 val default = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                 val conver = "yyyy.MM.dd"

                 val input = SimpleDateFormat(default, Locale.KOREA)
                 input.timeZone = TimeZone.getTimeZone("UTC")

                 val output = SimpleDateFormat(conver, Locale.KOREA)
                 val date = input.parse(time)

                 return output.format(date)
         } catch (e:Exception) {
             return "2022.00.00"
         }
    }
}

object TrainingManagementSwingVideoRecordsMapper
    : TrainingRecordMapper<TrainingManagementSummarySwingVideoResponse.TrainingManagementSummarySwingVideoRes,
        List<TRSummarySwingVideoRecordModel>> {
    override fun mapper(from: TrainingManagementSummarySwingVideoResponse.TrainingManagementSummarySwingVideoRes): List<TRSummarySwingVideoRecordModel> {
        val lst = mutableListOf<TRSummarySwingVideoRecordModel>()
        from.records.map { record ->
            if (record.svrUrl1 != null && isValidUrl(record.svrUrl1)) {
                val svr1 = TRSummarySwingVideoRecordModel(
                    id = record.id ?: 0,
                    club = record.club ?: "",
                    dist = record.dist ?: "",
                    carry = record.carry ?: "",
                    speed = record.speed ?: "",
                    angle = record.angle ?: "",
                    backspin = record.backspin ?: "",
                    shotType = record.shotType ?: "",
                    shotImageIndex = record.shotImageIndex ?: "4",
                    unit = record.unit ?: "meter",
                    shortUnit = record.shortUnit ?: "m",
                    svrUrl1 = record.svrUrl1 ?: "" ,
                    svrUrl2 = "",
                    regDate = record.regDate ?: "",
                    svrUrlThumbNail1 = record.svrUrlThumbNail1 ?: "",
                    svrUrlThumbNail2 = "",
                    videoPosition = "정면",
                )
                lst.add(svr1)
            }
            if (record.svrUrl2 != null && isValidUrl(record.svrUrl2)) {
               val svr2 =  TRSummarySwingVideoRecordModel(
                    id = record.id ?: 0,
                    club = record.club ?: "",
                    dist = record.dist ?: "",
                    carry = record.carry ?: "",
                    speed = record.speed ?: "",
                    angle = record.angle ?: "",
                    backspin = record.backspin ?: "",
                    shotType = record.shotType ?: "",
                  shotImageIndex = record.shotImageIndex ?: "4",
                   unit = record.unit ?: "meter",
                   shortUnit = record.shortUnit ?: "m",
                   svrUrl1 = record.svrUrl2 ?: "" ,
                    svrUrl2 =  "",
                    regDate = record.regDate ?: "",
                   svrUrlThumbNail1 = record.svrUrlThumbNail2 ?: "",
                   svrUrlThumbNail2 = "",
                   videoPosition = "측면",
                )
                lst.add(svr2)
            }
            val trSummarySwingVideoRecordModel = TRSummarySwingVideoRecordModel(
                id = record.id ?: 0,
                club = record.club ?: "",
                dist = record.dist ?: "",
                carry = record.carry ?: "",
                speed = record.speed ?: "",
                angle = record.angle ?: "",
                backspin = record.backspin ?: "",
                shotType = record.shotType ?: "",
                shotImageIndex = record.shotImageIndex ?: "4",
                unit = record.unit ?: "meter",
                shortUnit = record.shortUnit ?: "m",
                svrUrl1 = addPrefixHttp(record.svrUrl1),
                svrUrl2 = addPrefixHttp(record.svrUrl2),
                regDate = record.regDate ?: "",
                svrUrlThumbNail1 = "",
                svrUrlThumbNail2 = "",
                videoPosition = "정면",
            )
            trSummarySwingVideoRecordModel
        }
        return lst
    }
    private  fun isValidUrl(url:String?) :  Boolean {
        return try {
            if (url != null ) {
                val dirExtension = url.substringAfterLast('/',"")
                return if (dirExtension.isNotEmpty()) {
                    val fileExtension = dirExtension.substringAfterLast('.',"")
                    fileExtension.isNotEmpty()
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e:Exception) {
            false
        }
    }

    private  fun addPrefixHttp(url:String?) : String {
        try {
            if (url != null ) {
                val fileExtension = url.substringAfterLast('/',"")
                if (fileExtension.isNotEmpty()) {
//                    return "http://1.201.136.203/newhdd2/swing26/scr/gpv_prac_swmv_22419_116392_75812_0.png"
                    return "http://${url}"
                } else {
                    return ""
                }
            } else {
               return ""
            }
        } catch (e:Exception) {
            return ""
        }
    }
}


object TrainingManagementRoundDetailMapper
    : TrainingRecordMapper<TrainingManagementSummaryRoundDetailResponse.TrainingManagementSummaryRoundDetailRes,
        TRSummaryRoundDetailModel> {
    override fun mapper(from: TrainingManagementSummaryRoundDetailResponse.TrainingManagementSummaryRoundDetailRes):TRSummaryRoundDetailModel {
        if (from.records.count() == 18) {
            return TRSummaryRoundDetailModel(
                title = from.title ?: "",
                subTitle = from.subTitle ?: "",
                gmID = from.gmID ?: "",
                ccName = from.ccName ?: "",
                score = from.score ?: "",
                roundDate = convertedDate(from.roundDate ?:  "0000.00.00") ,
                avgDriverDist = from.avgDriverDist ?: "",
                avgDriverDistTitle = from.avgDriverDistTitle ?: "",
                avgDriverDistUnit = from.avgDriverDistUnit ?: "",
                inCourseScore = from.inCourseScore ?: "",
                outCourseScore = from.outCourseScore ?: "",
                longestShot = from.longestShot ?: "",
                longestShotTitle = from.longestShotTitle ?: "",
                longestShotUnit = from.longestShotUnit ?: "",
                onFairwayRate = from.onFairwayRate ?: "",
                onFairwayRateTitle = from.onFairwayRateTitle ?: "",
                onFairwayRateUnit = from.onFairwayRateUnit ?: "",
                onGreenRate = from.onGreenRate ?: "",
                onGreenRateTitle = from.onGreenRateTitle ?: "",
                onGreenRateUnit = from.onGreenRateUnit ?: "",
                avgPutt = from.avgPutt ?: "",
                avgPuttTitle = from.avgPuttTitle ?: "",
                avgPuttUnit = from.avgPuttUnit ?: "",
                parSave = from.parSave ?: "",
                parSaveTitle = from.parSaveTitle ?: "",
                parSaveUnit = from.parSaveUnit ?: "",
                hole1 = from.records[0].deltaScore ?: "",
                hole2 = from.records[1].deltaScore ?: "",
                hole3 = from.records[2].deltaScore ?: "",
                hole4 = from.records[3].deltaScore ?: "",
                hole5 = from.records[4].deltaScore ?: "",
                hole6 = from.records[5].deltaScore ?: "",
                hole7 = from.records[6].deltaScore ?: "",
                hole8 = from.records[7].deltaScore ?: "",
                hole9 = from.records[8].deltaScore ?: "",
                hole10 = from.records[9].deltaScore ?: "",
                hole11 = from.records[10].deltaScore ?: "",
                hole12 = from.records[11].deltaScore ?: "",
                hole13 = from.records[12].deltaScore ?: "",
                hole14 = from.records[13].deltaScore ?: "",
                hole15 = from.records[14].deltaScore ?: "",
                hole16 = from.records[15].deltaScore ?: "",
                hole17 = from.records[16].deltaScore ?: "",
                hole18 = from.records[17].deltaScore ?: "",
            )

        } else {
            return TRSummaryRoundDetailModel(
                title = from.title ?: "",
                subTitle = from.subTitle ?: "",
                gmID = from.gmID ?: "",
                ccName = from.ccName ?: "",
                score = from.score ?: "",
                roundDate = convertedDate(from.roundDate ?:  "0000.00.00") ,
                avgDriverDist = from.avgDriverDist ?: "",
                avgDriverDistTitle = from.avgDriverDistTitle ?: "",
                avgDriverDistUnit = from.avgDriverDistUnit ?: "",
                inCourseScore = from.inCourseScore ?: "",
                outCourseScore = from.outCourseScore ?: "",
                longestShot = from.longestShot ?: "",
                longestShotTitle = from.longestShotTitle ?: "",
                longestShotUnit = from.longestShotUnit ?: "",
                onFairwayRate = from.onFairwayRate ?: "",
                onFairwayRateTitle = from.onFairwayRateTitle ?: "",
                onFairwayRateUnit = from.onFairwayRateUnit ?: "",
                onGreenRate = from.onGreenRate ?: "",
                onGreenRateTitle = from.onGreenRateTitle ?: "",
                onGreenRateUnit = from.onGreenRateUnit ?: "",
                avgPutt = from.avgPutt ?: "",
                avgPuttTitle = from.avgPuttTitle ?: "",
                avgPuttUnit = from.avgPuttUnit ?: "",
                parSave = from.parSave ?: "",
                parSaveTitle = from.parSaveTitle ?: "",
                parSaveUnit = from.parSaveUnit ?: "",
            )
        }
    }

//    fun roundRecords(from :  TrainingManagementSummaryRoundDetailResponse.TrainingManagementSummaryRoundDetailRes)
//    : List<TRSummaryRoundDetailRecordModel> {
//        return from.records.map { record ->
//            TRSummaryRoundDetailRecordModel(
//             holeIndex = record.holeIndex ?: "",
//             par = record.par ?: "",
//             score = record.score ?: "",
//             deltaScore = record.deltaScore ?: ""
//            )
//        }
//    }

    private  fun convertedDate(time:String) : String {
        try {
            val default = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val conver = "yyyy.MM.dd"

            val input = SimpleDateFormat(default, Locale.KOREA)
            input.timeZone = TimeZone.getTimeZone("UTC")

            val output = SimpleDateFormat(conver, Locale.KOREA)
            val date = input.parse(time)

            return output.format(date)
        } catch (e:Exception) {
            return "2022.00.00"
        }
    }
}


object TRMyGolfPowerExamResultPageInfoModelMapper
    : TrainingRecordMapper<MyGolfPowerExamResultPageInfoResponse.MyGolfPowerExamResultPageInfoRes, TRMyGolfPowerExamResultPageInfoModel> {
    override fun mapper(from: MyGolfPowerExamResultPageInfoResponse.MyGolfPowerExamResultPageInfoRes): TRMyGolfPowerExamResultPageInfoModel {
        return  TRMyGolfPowerExamResultPageInfoModel(
            pageCount = from.pageCount?.toInt() ?: 0,
            currentPageIndex = from.currentPageIndex?.toInt() ?: -1
        )
    }
}

