package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.ui.comment.CommentViewModel
import com.artilearn.happygolfgo.ui.common.DeveloperOptionViewModel
import com.artilearn.happygolfgo.ui.golfgame.GolfGameViewModel
import com.artilearn.happygolfgo.ui.golfrank.GolfRankViewModel
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.artilearn.happygolfgo.ui.home.alram.AlramViewModel
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterViewModel
import com.artilearn.happygolfgo.ui.home.lecture.CommentListViewModel
import com.artilearn.happygolfgo.ui.home.mypage.MyPageViewModel
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.ProfileChangeViewModel
import com.artilearn.happygolfgo.ui.home.record.*
import com.artilearn.happygolfgo.ui.home.reservation.ReservationViewModel
import com.artilearn.happygolfgo.ui.home.lecture.LectureDetailViewModel
import com.artilearn.happygolfgo.ui.home.lecture.LectureSectionViewModel
import com.artilearn.happygolfgo.ui.home.lecture.LectureFragmentViewModel
import com.artilearn.happygolfgo.ui.home.lecture.LectureMyCommentListViewModel
import com.artilearn.happygolfgo.ui.home.lecture.LectureMyLikeViewModel
import com.artilearn.happygolfgo.ui.home.lecture.LectureMyPickViewModel
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListViewModel
import com.artilearn.happygolfgo.ui.log.LogViewModel
import com.artilearn.happygolfgo.ui.login.LoginViewModel
import com.artilearn.happygolfgo.ui.myinfo.MyInfoViewModel
import com.artilearn.happygolfgo.ui.pwauth.PwAuthViewModel
import com.artilearn.happygolfgo.ui.pwreset.PwResetViewModel
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmViewModel
import com.artilearn.happygolfgo.ui.reservelist.ReserveListViewModel
import com.artilearn.happygolfgo.ui.reservelist.fragment.ReserveLessonViewModel
import com.artilearn.happygolfgo.ui.reservelist.fragment.ReservePlateViewModel
import com.artilearn.happygolfgo.ui.splash.SplashViewModel
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateViewModel
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeViewModel
import com.artilearn.happygolfgo.ui.tutorial.TutorialViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { TutorialViewModel(get()) }

    viewModel { TicketTimeViewModel(get()) }

    viewModel { TicketDateViewModel(get()) }

    viewModel { SplashViewModel(get(), get()) }

    viewModel { ReserveListViewModel(get()) }

    viewModel { ReservePlateViewModel() }

    viewModel { ReserveLessonViewModel() }

    viewModel { ReserveConfirmViewModel(get(), get(), get(), get()) }

    viewModel { PwResetViewModel(get()) }

    viewModel { PwAuthViewModel(get()) }

    viewModel { MyPageViewModel(get(), get()) }

    viewModel { LectureFragmentViewModel(get(), get() , get() ) }

    viewModel { LectureDetailViewModel(get(), get() , get() ) }

    viewModel { LectureSectionViewModel(get(), get() , get() ) }

    viewModel { LectureMyPickViewModel(get(), get() , get() ) }

    viewModel { LectureMyLikeViewModel(get(), get() , get() ) }

    viewModel { LectureMyCommentListViewModel(get() ) }


    viewModel { MyInfoViewModel(get()) }


    viewModel { LoginViewModel(get(), get()) }

    viewModel { LogViewModel(get()) }

    viewModel { TrainingRecordRoundDetailViewModel(get()) }

    viewModel { TrainingRecordSwingVideoPlayerViewModel(get()) }

    viewModel { LessonPostListViewModel(get()) }

    viewModel { TrainingRecordSwingVideoListViewModel(get()) }

    viewModel { HomeViewModel() }

    viewModel { ReservationViewModel(get(), get()) }

    viewModel { GameCenterViewModel(get()) }
    //vc45  게임로그 뷰모모델에 대한 Dependency Injection
    viewModel { TrainingRecordViewModel(get()) }
    //Adding
    viewModel { MyGolfPowerRankingViewModel(get()) }

    viewModel { GolfRankingInBranchViewModel(get())}

    viewModel { GolfRankingBySubjectViewModel(get()) }

    viewModel { GolfRankingInAllBranchViewModel(get()) }

    viewModel { MyGolfPowerViewModel(get())}

    viewModel { AlramViewModel(get()) }

    viewModel { CommentListViewModel(get()) }

    viewModel { GolfRankViewModel(get()) }

    viewModel { CommentViewModel(get()) }

    viewModel { DeveloperOptionViewModel(get()) }

    viewModel { GolfGameViewModel(get()) }

    viewModel { TicketManagerViewModel(get()) }

    viewModel { ProfileChangeViewModel(get(), androidApplication()) }
}