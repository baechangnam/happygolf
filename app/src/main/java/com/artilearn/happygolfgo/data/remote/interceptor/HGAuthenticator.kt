package com.artilearn.happygolfgo.data.remote.interceptor

import android.util.Log
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.util.PreferenceManager
import com.artilearn.happygolfgo.util.getWidevineID
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

// https://medium.com/@olempico/okhttp-authenticator-lets-be-secure-in-an-elegant-way-3bf6096752dc
// https://objectpartners.com/2018/06/08/okhttp-authenticator-selectively-reauthorizing-requests/
//class HGAuthenticator(
//    private val api: ApiInterface,
//    private val preferenceManager: PreferenceManager
//) : HGAuth, Authenticator {
//
//    private val compositeDisposable = CompositeDisposable()
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//        val request = chain.request().newBuilder()
//            .addHeader("Authorization", "bearer ${tokenSource.getToken()}")
//            .build()
//        val response = chain.proceed(request)// when getting error response then continuing operation by refreshing the token
//        // and repeating the request with refreshed data
//        if (response.code() == 401) {
//            val newRequest = chain.request().newBuilder()
//                .addHeader("Authorization", "bearer ${tokenSource.refreshToken()}")
//                .build()
//            return chain.proceed(newRequest)
//        }return response
//
////        if (response.code == 200) {
////
////        } else {
////            api.getAccessToken(createData())
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe({
////                    val body = it.body()
////                    if (it.isSuccessful && body != null) {
////                        preferenceManager.accessToken = body.data.accessToken
////                    }
////                }, {
////                    Log.d("error", "authenticate: ${it.localizedMessage}")
////                }).addTo(compositeDisposable)
////        }
//    }
//
////    override fun renewToken(map: HashMap<String, SplashTokenModel>): Single<SplashTokenResponse> {
////        return Single.create { emitter ->
////            api.getAccessToken(createData())
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .doOnSuccess {
////                    val body = it.body()
////                    if (it.isSuccessful && body != null) {
////                        preferenceManager.accessToken = body.data.accessToken
////
////                    }
////                }
////                .doOnError { emitter.onError(it) }
////        }
////    }
//
//    private fun createData(): HashMap<String, SplashTokenModel> {
//        val model = SplashTokenModel(
//            preferenceManager.clientScreetKey,
//            getWidevineID()
//        )
//
//        val hashMap = hashMapOf<String, SplashTokenModel>()
//        hashMap["Data"] = model
//
//        return hashMap
//    }
//
//}