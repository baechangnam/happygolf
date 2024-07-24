package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.util.NetworkManager
import org.koin.dsl.module

val networkModule = module {
    single { NetworkManager(get()) }
}