package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.data.developeroption.source.local.DeveloperOptionDataSoucre
import com.artilearn.happygolfgo.data.developeroption.source.local.DeveloperOptionDataSoucreImpl
import com.artilearn.happygolfgo.data.gamecenter.source.local.GameCenterLocalDataSource
import com.artilearn.happygolfgo.data.gamecenter.source.local.GameCenterLocalDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSource
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSourceImpl
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSource
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSourceImpl
import com.artilearn.happygolfgo.data.profilechange.source.local.ProfileChangeLocalDataSource
import com.artilearn.happygolfgo.data.profilechange.source.local.ProfileChangeLocalDataSourceImpl
import com.artilearn.happygolfgo.data.pwauth.source.local.PwAuthLocalDataSource
import com.artilearn.happygolfgo.data.pwauth.source.local.PwAuthLocalDataSourceImpl
import com.artilearn.happygolfgo.data.pwreset.source.local.PwrestLocalDataSource
import com.artilearn.happygolfgo.data.pwreset.source.local.PwrestLocalDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSourceImpl
import com.artilearn.happygolfgo.data.ticketdate.source.local.TicketDateLocalDataSource
import com.artilearn.happygolfgo.data.ticketdate.source.local.TicketDateLocalDataSourceImpl
import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSource
import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSourceImpl
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSource
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSourceImpl
import com.artilearn.happygolfgo.util.CurrentVersion
import com.artilearn.happygolfgo.util.PreferenceManager
import org.koin.dsl.module

val localModule = module {

    single { PreferenceManager(get()) }

    single<LoginLocalDataSource> { LoginLocalDataSourceImpl(get()) }

    single<SplashLocalDataSource> { SplashLocalDataSourceImpl(get()) }

    single<TutorialLocalDataSource> { TutorialLocalDataSourceImpl(get()) }

    single<VersionLocalDataSource> { VersionLocalDataSourceImpl(get()) }

    single<MyInfoLocalDataSource> { MyInfoLocalDataSourceImpl(get()) }

    single<PwrestLocalDataSource> { PwrestLocalDataSourceImpl(get()) }

    single<DeveloperOptionDataSoucre> { DeveloperOptionDataSoucreImpl(get()) }

    single<GameCenterLocalDataSource> { GameCenterLocalDataSourceImpl(get()) }

    single<PwAuthLocalDataSource> { PwAuthLocalDataSourceImpl(get()) }

    single { CurrentVersion(get()) }

    single<ProfileChangeLocalDataSource> { ProfileChangeLocalDataSourceImpl(get()) }

    single<TicketDateLocalDataSource> { TicketDateLocalDataSourceImpl(get()) }
}