package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import com.artilearn.happygolfgo.data.alram.source.AlramRepositoryImpl
import com.artilearn.happygolfgo.data.comment.source.CommentRepository
import com.artilearn.happygolfgo.data.comment.source.CommentRepositoryImpl
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepository
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepositoryImpl
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepositoryImpl
import com.artilearn.happygolfgo.data.golfgame.source.GolfGameRepository
import com.artilearn.happygolfgo.data.golfgame.source.GolfGameRepositoryImpl
import com.artilearn.happygolfgo.data.golfrank.source.GolfRankRepository
import com.artilearn.happygolfgo.data.golfrank.source.GolfRankRepositoryImpl
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.data.home.source.HomeRepositoryImpl
import com.artilearn.happygolfgo.data.lessonlog.source.LessonLogRepository
import com.artilearn.happygolfgo.data.lessonlog.source.LessonLogRepositoryImpl
import com.artilearn.happygolfgo.data.log.source.LogRepository
import com.artilearn.happygolfgo.data.log.source.LogRepositoryImpl
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.data.login.source.LoginRepositoryImpl
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepository
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepositoryImpl
import com.artilearn.happygolfgo.data.profilechange.source.ProfileChangeRepository
import com.artilearn.happygolfgo.data.profilechange.source.ProfileChangeRepositoryImpl
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepositoryImpl
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepository
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepositoryImpl
import com.artilearn.happygolfgo.data.reserveconfirm.source.*
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepository
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepositoryImpl
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.data.splash.source.SplashRepositoryImpl
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepository
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepositoryImpl
import com.artilearn.happygolfgo.data.ticketmanager.source.TicketManagerRepository
import com.artilearn.happygolfgo.data.ticketmanager.source.TicketManagerRepositoryImpl
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepositoryImpl
import com.artilearn.happygolfgo.data.lecture.source.LectureRepository
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepository
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepositoryImpl
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.data.version.source.VersionRepositoryImpl
import com.artilearn.happygolfgo.data.lecture.source.LectureRepositoryImpl

import org.koin.dsl.module
//comments: IoC  네트워크 레파지토리 추가될때 여기에 추가한다.
val repositoryModule = module {

    single<LoginRepository> { LoginRepositoryImpl(get(), get(), get()) }

    single<SplashRepository> { SplashRepositoryImpl(get(), get(), get(), get()) }

    single<TutorialRepository> { TutorialRepositoryImpl(get()) }

    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }

    single<VersionRepository> { VersionRepositoryImpl(get(), get(), get(), get()) }

    single<MyInfoRepository> { MyInfoRepositoryImpl(get(), get(), get()) }

    single<PwAuthRepository> { PwAuthRepositoryImpl(get(), get(), get()) }

    single<PwrestRepository> { PwrestRepositoryImpl(get(), get(), get()) }

    single<LessonLogRepository> { LessonLogRepositoryImpl(get(), get()) }

    single<AlramRepository> { AlramRepositoryImpl(get(), get()) }

    single<TicketDateRepository> { TicketDateRepositoryImpl(get(), get(), get()) }

    single<TicketTimeRepository> { TicketTimeRepositoryImpl(get(), get()) }

    single<ReserveConfirmRepository> { ReserveConfirmRepositoryImpl(get(), get(), get(), get(), get()) }

    single<ReservationRepository> { ReserveConfirmRepositoryImpl(get(), get(), get(), get(), get()) }

    single<ReserveListRepository> { ReserveListRepositoryImpl(get(), get()) }

    single<CancelRepository> { ReserveConfirmRepositoryImpl(get(), get(), get(), get(), get()) }

    single<LogRepository> { LogRepositoryImpl(get(), get()) }

    single<CommentRepository> { CommentRepositoryImpl(get(), get()) }

    single<DeveloperOptionRepository> { DeveloperOptionRepositoryImpl(get()) }

    single<TimeChangeRepository> { ReserveConfirmRepositoryImpl(get(), get(), get(), get(), get()) }

    single<GameCenterRepository> { GameCenterRepositoryImpl(get(), get(), get()) }
    
    single<GolfRankRepository> { GolfRankRepositoryImpl(get(), get()) }

    single<GolfGameRepository> { GolfGameRepositoryImpl(get(), get()) }

    single<ProfileChangeRepository> { ProfileChangeRepositoryImpl(get(), get()) }

    single<TicketManagerRepository> { TicketManagerRepositoryImpl(get(), get()) }

    single<LectureRepository> { LectureRepositoryImpl(get(), get()) }
}