package com.artilearn.happygolfgo.modules

//import com.google.firebase.iid.FirebaseInstanceId //deprecated
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module

val firebaseModule = module {
//    single { FirebaseInstanceId.getInstance() }
    single { FirebaseMessaging.getInstance() }
}