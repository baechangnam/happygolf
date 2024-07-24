package com.artilearn.happygolfgo.data.login.source.remote

import android.text.TextUtils
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.modules.apiModule
//import com.google.firebase.iid.FirebaseInstanceId // adjusting deprecated api removed
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Single
import retrofit2.Retrofit

class LoginRemoteDataSourceImpl(
    private val apiInterface: ApiInterface,
//    private val firebase: FirebaseInstanceId
    private val firebase: FirebaseMessaging
) : LoginRemoteDataSource {

    override fun remoteLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return apiInterface.login(data)
    }

    override fun getFcmToken(): Single<String?> {
//            FirebaseMessaging.getInstance().token --> Dependency injected in FirebaseModule.kt
//            return firebase.instanceId
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