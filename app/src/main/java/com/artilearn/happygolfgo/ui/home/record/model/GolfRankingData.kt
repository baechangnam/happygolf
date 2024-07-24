package com.artilearn.happygolfgo.ui.home.record.model

import com.artilearn.happygolfgo.data.gamecenter.GolfRankingAllBranchResponse
import com.artilearn.happygolfgo.data.gamecenter.GolfRankingInBranchResponse
import com.artilearn.happygolfgo.data.gamecenter.GolfRankingSubjectResponse
import com.artilearn.happygolfgo.data.gamecenter.MyGolfPowerExamResultPageInfoDetailResponse

data class RankingInBranchData (
    val title: String = "",
    val firstName:String = "-",
    val firstScore:String = "-",
    val secondName:String = "-",
    val secondScore:String = "-",
    val thirdName:String = "-",
    val thirdScore:String = "-"
) {
    companion object {
//         public fun createDefaults() : ArrayList<RankingInBranchData> {
//             return arrayListOf(
//               RankingInBranchData("5과목\n평균", "김*성", "89","이*", "70", "김*", "30")
//              ,RankingInBranchData("PUTT력", "김*성", "89","이*", "70", "김*", "30")
//             ,RankingInBranchData("S/G력", "김*성", "89","이*", "70", "김*", "30")
//             ,RankingInBranchData("IRON력", "김*성", "89","이*", "70", "김*", "30")
//             ,RankingInBranchData("DRV력", "김*성", "89","이*", "70", "김*", "30")
//             ,RankingInBranchData("W/U 력", "김*성", "89","이*", "70", "김*", "30")
//             )
//         }

        fun map(from: GolfRankingInBranchResponse.GolfRankingInBranchRes) : ArrayList<RankingInBranchData> {
             val lst = mutableListOf<RankingInBranchData>()
             from.rankings.map { ranking ->
                 lst.add(
                 RankingInBranchData(
                     ranking.title ?: "5과목\n평균"
                    ,ranking.firstName ?: "-"
                    , ranking.firstScore ?: "-"
                    ,ranking.secondName ?: "-"
                    , ranking.secondScore ?: "-"
                    , ranking.thirdName ?: "-"
                    , ranking.thirdScore ?: "-"))
            }
            return lst as ArrayList<RankingInBranchData>
        }
    }
}

//참여도
data class RankingBySubjectData (
    val  rankingOrder: String = "1",
    val  branchName:String = "-",
    val  area:String = "-",
    val  sumOfPlayersScoreInfo:String = "-",
    val  participationRate:String = "-%"
) {
    companion object {
//        public fun createDefaults() : Array<RankingBySubjectData> {
//                 return arrayOf(
//                     RankingBySubjectData("1", "대구혁신점", "22%","220명")
//                    ,RankingBySubjectData("2", "일산덕이점", "21%","210명")
//                     ,RankingBySubjectData("3", "가산디지털점", "19%","190명")
//                     ,RankingBySubjectData("4", "평촌점", "12%","180명")
//                     ,RankingBySubjectData("5", "역삼점", "40%","170명")
//                 )
//        }
        fun map(from: GolfRankingSubjectResponse.GolfRankingSubjectRes) : ArrayList<RankingBySubjectData> {
            val lst = mutableListOf<RankingBySubjectData>();
            from.rankings.map { ranking ->
                lst.add(
                    RankingBySubjectData(
                        ranking.rankingOrder ?: "-",
                        ranking.branchName ?: "-",
                        ranking.area ?: "",
                        ranking.sumOfPlayersScoreInfo ?: "-",
                        ranking.participationRate ?: "-"
                    )
                )
            }
            return lst as ArrayList<RankingBySubjectData>
        }
    }
}

data class RankingInAllBranchData (
    val title: String = "",
    val firstBranchName:String = "-",
    val firstBranchScore:String = "-",
    val secondBranchName:String = "-",
    val secondBranchScore:String = "-",
    val thirdBranchName:String = "-",
    val thirdBranchScore:String = "-"
) {
    companion object {
//        public fun createDefaults() : Array<RankingInAllBranchData> {
//            return arrayOf(
//                RankingInAllBranchData("5과목\n평균", "1가산디지털점", "89","배곧서울대점*", "70", "대구테크노점", "30")
//                ,RankingInAllBranchData("PUTT력", "2배곧서울대점","89","평촌점*", "70", "배곧서울대점", "30")
//                ,RankingInAllBranchData("S/G력", "3대구테크노점", "89","동탄삼성점*", "70", "대구테크노점", "30")
//                ,RankingInAllBranchData("IRON력", "4평촌점", "89","평촌점*", "70", "평촌점*", "30")
//                ,RankingInAllBranchData("DRV력", "대구테크노점", "89","대구테크노점*", "70", "대구테크노점", "30")
//                ,RankingInAllBranchData("W/U 력", "동탄삼성점", "89","이*", "70", "동탄삼성점", "30")
//            )
//        }

        fun map(from:  GolfRankingAllBranchResponse.GolfRankingAllBranchRes) : ArrayList<RankingInAllBranchData> {
            val lst = mutableListOf<RankingInAllBranchData>()
            from.rankings.map { ranking ->
                lst.add(
                    RankingInAllBranchData(
                        ranking.title ?: ""
                    , ranking.firstBranchName ?: ""
                    , ranking.firstBranchScore ?: ""
                    , ranking.secondBranchName ?: ""
                    , ranking.secondBranchScore ?: ""
                    , ranking.thirdBranchName ?: ""
                    , ranking.thirdBranchScore ?: ""
                    ))
            }
            return lst as ArrayList<RankingInAllBranchData>
        }
    }
}

data class MyRankingByGameTypeAvg (
    val title : String = ""
    ,val rankingInMyBranch:String  = "-"
    ,val rankingInAllBranch:String = "-"
    ,val score:String = "-"
    ,val indicator:String = "even"  //등락 표시  "up", "down", "even"
    ,val delta:String = "-" //10점
    ,var numberOfPlayersInMyBranch :  String  = "-" //122명 참여
    ,var numberOfPlayersInAllBranch: String = "-"//1123명 참여
    ,var commentBetterThan:String ="" //3월보다
    ,var commentSummary:String = "" //훈련성과가 상위 13%
) {
//    companion object {
//        public fun createDefaults(title:String = ""
//                                  , rankingInMyBranch:String = ""
//                                  , rankingInAllBranch: String = ""
//                                  , score:String = ""
//                                  , indicator:String = ""
//                                  , delta :String = ""
//                                  , numberOfPlayersInMyBranch:String = ""
//                                  , numberOfPlayersInAllBranch:String = ""
//        ) :  MyRankingByGameTypeAvg {
//            return MyRankingByGameTypeAvg(title, rankingInMyBranch, rankingInAllBranch, score, indicator, delta
//            ,numberOfPlayersInMyBranch, numberOfPlayersInAllBranch)
//        }
//    }
}

data class MyRankingByGameType (
     val title : String = ""
     , val rankingInMyBranch:String  = "-"
      , val rankingInAllBranch:String = "-"
      , val score:String = "-"
      , val indicator:String = "even"  //등락 표시  "up", "down", "even"
      , val delta:String = "-"
      , val iconType:String = "0"
      , val hideComments:String ="Y"
    , val commentSummary:String="-"
        ) {
//    companion object {
//        public fun createDefaults(title:String = ""
//       , rankingInMyBranch:String = ""
//        , rankingInAllBranch: String = ""
//        , score:String = ""
//        , indicator:String = ""
//        , delta :String = ""
//        ) :  MyRankingByGameType {
//            return MyRankingByGameType(title, rankingInMyBranch, rankingInAllBranch, score, indicator, delta)
//        }
//    }
}

data class MyRankingBoardData (
  var index : String = "0"
, var title : String = "-월 시험"
, var isOpen: String = "Y"    //Y/N
, var noticeOfNextExam:String  = "4월 20일 예정입니다."
, var avg : MyRankingByGameTypeAvg =  MyRankingByGameTypeAvg()
, var putt: MyRankingByGameType =  MyRankingByGameType()
, var sg: MyRankingByGameType =  MyRankingByGameType()
, var iron: MyRankingByGameType =  MyRankingByGameType()
, var drv: MyRankingByGameType =  MyRankingByGameType()
, var wu: MyRankingByGameType =  MyRankingByGameType()
) {

    val isHiddenTop: Boolean
        get() {
            return  isOpen.uppercase() == "Y"
        }

    val isHiddenBottom: Boolean
        get() {
            return  isOpen.uppercase() != "Y"
        }

     companion object  {
//         public fun createDefaults(index:String, title:String, isOpen:String = "Y",
//
//                                   ) : MyRankingBoardData {
//             return MyRankingBoardData(
//                  index,
//                   title,
//                   isOpen,
//                 "준비중입니다.",
//                 MyRankingByGameTypeAvg.createDefaults("5과목 평균", "", "", "", "even", "") //avg
//                  ,MyRankingByGameType.createDefaults("PUTT 력", "", "", "", "even", "") //avg
//                 ,MyRankingByGameType.createDefaults("S/G 력", "", "", "", "even", "") //avg
//                 ,MyRankingByGameType.createDefaults("IRON 력", "", "", "", "even", "") //avg
//                 ,MyRankingByGameType.createDefaults("DRV 력", "", "", "", "even", "") //avg
//                 ,MyRankingByGameType.createDefaults("W/U 력", "", "", "", "even", "") //avg
//             )
//         }
//         public fun createDummyDefault(index:String, title:String, isOpen:String = "Y",
//
//                                   ) : MyRankingBoardData {
//             return MyRankingBoardData(
//                 index,
//                 title,
//                 isOpen,
//                 "4월 20일 예정 입니다.",
//                 MyRankingByGameTypeAvg.createDefaults("5과목 평균", "10", "1,100", "40", "up", "10") //avg
//                 ,MyRankingByGameType.createDefaults("PUTT 력", "30", "1,200", "70", "even", "10") //avg
//                 ,MyRankingByGameType.createDefaults("S/G 력", "40", "2,210", "80", "down", "20") //avg
//                 ,MyRankingByGameType.createDefaults("IRON 력", "40", "3,400", "80", "down", "20") //avg
//                 ,MyRankingByGameType.createDefaults("DRV 력", "1", "23", "95", "up", "30") //avg
//                 ,MyRankingByGameType.createDefaults("W/U 력", "10", "230", "95", "down", "5") //avg
//             )
//         }

         fun  from( res:MyGolfPowerExamResultPageInfoDetailResponse.MyGolfPowerExamResultPageInfoDetailRes)
         :MyRankingBoardData
         {
             val rankingBoard = MyRankingBoardData(
                res.pageInfo.index ?: "0",
                 res.pageInfo.title ?: "",
                 res.pageInfo.isOpen ?: "Y",
                 res.pageInfo.noticeOfNextExam ?:"준비중",
             )

             res.avg?.let {
                   rankingBoard.avg = mapToMyRankingByGameTypeAvg(it)
             }

             res.putt?.let {
                  rankingBoard.putt = mapToMyRankingByGameType(it)
             }

             res.sg?.let {
                 rankingBoard.sg = mapToMyRankingByGameType(it)
             }

             res.iron?.let {
                 rankingBoard.iron = mapToMyRankingByGameType(it)
             }

             res.drv?.let {
                 rankingBoard.drv = mapToMyRankingByGameType(it)
             }

             res.wu?.let {
                 rankingBoard.wu = mapToMyRankingByGameType(it)
             }
             return rankingBoard
         }

        private fun mapToMyRankingByGameType(it: MyGolfPowerExamResultPageInfoDetailResponse.MyGolfPowerExamResultPageInfoDetailResPageInfoGameRanking)
        : MyRankingByGameType {
            return  MyRankingByGameType(
                it.title ?: "",
                it.rankingInMyBranch ?: "",
                it.rankingInAllBranch ?: "",
                it.score ?: "",
                it.indicator ?: "even",
                it.delta  ?: "",
                it.iconType ?: "0",
                it.hideComments ?: "Y",
                it.commentSummary ?: "-"
            )
        }

         private fun mapToMyRankingByGameTypeAvg(it: MyGolfPowerExamResultPageInfoDetailResponse.MyGolfPowerExamResultPageInfoDetailResPageInfoGameRankingAvg)
         : MyRankingByGameTypeAvg {
             return  MyRankingByGameTypeAvg(
                 it.title ?: "",
                 it.rankingInMyBranch ?: "",
                 it.rankingInAllBranch ?: "",
                 it.score ?: "",
                 it.indicator ?: "even",
                 it.delta ?: "",
                 it.numberOfPlayersInMyBranch ?: "-",
                 it.numberOfPlayersInAllBranch ?: "-"
                 ,it.commentBetterThan ?: "-"
                 ,it.commentSummary ?: "-"
             )
         }
     }
}
