package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.data.alram.source.remote.AlramRemoteDataSource
import com.artilearn.happygolfgo.data.alram.source.remote.AlramRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.comment.source.remote.CommentRemoteDataSource
import com.artilearn.happygolfgo.data.comment.source.remote.CommentRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.gamecenter.source.remote.GameCenterRemoteDataSource
import com.artilearn.happygolfgo.data.gamecenter.source.remote.GameCenterRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.golfgame.source.remote.GolfGameRemoteDataSource
import com.artilearn.happygolfgo.data.golfgame.source.remote.GolfGameRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.golfrank.source.remote.GolfRankRemoteDataSource
import com.artilearn.happygolfgo.data.golfrank.source.remote.GolfRankRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSource
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.lessonlog.source.remote.LessonLogRemoteDataSource
import com.artilearn.happygolfgo.data.lessonlog.source.remote.LessonLogRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.log.source.remote.LogRemoteDataSource
import com.artilearn.happygolfgo.data.log.source.remote.LogRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSource
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.profilechange.source.remote.ProfileChangeRemoteDataSource
import com.artilearn.happygolfgo.data.profilechange.source.remote.ProfileChangeRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.pwauth.source.remote.PwAuthRemoteDataSource
import com.artilearn.happygolfgo.data.pwauth.source.remote.PwAuthRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.pwreset.source.remote.PwrestRemoteDataSource
import com.artilearn.happygolfgo.data.pwreset.source.remote.PwrestRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.*
import com.artilearn.happygolfgo.data.reservelist.source.remote.ReserveListRemoteDataSource
import com.artilearn.happygolfgo.data.reservelist.source.remote.ReserveListRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.ticketdate.source.remote.TicketDateRemoteDataSource
import com.artilearn.happygolfgo.data.ticketdate.source.remote.TicketDateRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.ticketmanager.source.remote.TicketManagerRemoteDataSource
import com.artilearn.happygolfgo.data.ticketmanager.source.remote.TicketManagerRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.tickettime.source.remote.TicketTimeRemoteDataSource
import com.artilearn.happygolfgo.data.tickettime.source.remote.TicketTimeRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.lecture.source.remote.LectureRemoteDataSource
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSource
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSourceImpl
import org.koin.dsl.module
import com.artilearn.happygolfgo.data.lecture.source.remote.LectureRemoteDataSourceImpl

val remoteModule = module {

    single<LoginRemoteDataSource> { LoginRemoteDataSourceImpl(get(), get()) }

    single<SplashRemoteDataSource> { SplashRemoteDataSourceImpl(get(), get()) }

    single<HomeRemoteDataSource> { HomeRemoteDataSourceImpl(get()) }

    single<VersionRemoteDataSource> { VersionRemoteDataSourceImpl(get()) }

    single<MyInfoRemoteDataSource> { MyInfoRemoteDataSourceImpl(get()) }

    single<PwAuthRemoteDataSource> { PwAuthRemoteDataSourceImpl(get()) }

    single<PwrestRemoteDataSource> { PwrestRemoteDataSourceImpl(get()) }

    single<LessonLogRemoteDataSource> { LessonLogRemoteDataSourceImpl(get()) }

    single<AlramRemoteDataSource> { AlramRemoteDataSourceImpl(get()) }

    single<TicketDateRemoteDataSource> { TicketDateRemoteDataSourceImpl(get()) }

    single<TicketTimeRemoteDataSource> { TicketTimeRemoteDataSourceImpl(get()) }

    single<ReserveConfirmRemoteDataSource> { ReserveConfirmRemoteDataSourceImpl(get()) }

    single<ReservationRemoteDataSource> { ReserveConfirmRemoteDataSourceImpl(get()) }

    single<ReserveListRemoteDataSource> { ReserveListRemoteDataSourceImpl(get()) }

    single<CancelRemoteDataSource> { ReserveConfirmRemoteDataSourceImpl(get()) }

    single<LogRemoteDataSource> { LogRemoteDataSourceImpl(get()) }

    single<CommentRemoteDataSource> { CommentRemoteDataSourceImpl(get()) }

    single<TimeChangeRemoteSource> { ReserveConfirmRemoteDataSourceImpl(get()) }

    single<GameCenterRemoteDataSource> { GameCenterRemoteDataSourceImpl(get()) }

    single<GolfRankRemoteDataSource> { GolfRankRemoteDataSourceImpl(get()) }

    single<GolfGameRemoteDataSource> { GolfGameRemoteDataSourceImpl(get()) }

    single<ProfileChangeRemoteDataSource> { ProfileChangeRemoteDataSourceImpl(get()) }

    single<TicketManagerRemoteDataSource> { TicketManagerRemoteDataSourceImpl(get()) }

    single<LectureRemoteDataSource> { LectureRemoteDataSourceImpl(get()) }
}