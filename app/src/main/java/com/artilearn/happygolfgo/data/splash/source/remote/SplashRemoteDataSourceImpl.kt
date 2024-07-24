package com.artilearn.happygolfgo.data.splash.source.remote

import android.text.TextUtils
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
//import com.google.firebase.iid.FirebaseInstanceId //deprecated api, use FirebaeMessaging instead of FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Single
import retrofit2.Response

class SplashRemoteDataSourceImpl(
    private val apiInterface: ApiInterface,
//    private val firebase: FirebaseInstanceId
    private val firebase: FirebaseMessaging
) : SplashRemoteDataSource {

    override fun remoteGetAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>> {
        return apiInterface.getAccessToken(data)
    }

    override fun remoteUserSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>> {
        return apiInterface.userSynchronized(data)
    }

//    override fun deleteUser(): Completable {
//        return apiInterface.userLogout()
//    }

    override fun getFcmToken(): Single<String?> {
//        return Single.create { emitter ->
//            firebase.instanceId
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        task.result?.let {
//                            emitter.onSuccess(it.token)
//                        } ?: emitter.onSuccess("")
//                    } else {
//                        task.exception?.let { t ->
//                            emitter.onError(t)
//                        } ?: emitter.onError(Throwable(IllegalStateException("UnKnown Error")))
//                    }
//                }
//        }
        return Single.create { emitter ->
            firebase.token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null && !TextUtils.isEmpty(task.result)) {
                            val token: String = task.result!!
                            emitter.onSuccess(token);
                        } else {
                            emitter.onSuccess("");
                        }
                    } else {
                        task.exception?.let { t ->
                            emitter.onError(t)
                        } ?: emitter.onError(Throwable(IllegalStateException("UnKnown Error")))
                    }
                }
        }

    }
}