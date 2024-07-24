package com.artilearn.happygolfgo.data.remote.interceptor

import com.artilearn.happygolfgo.util.CurrentVersion
import com.artilearn.happygolfgo.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class HGTokenInterceptor(
    private val preferenceManager: PreferenceManager,
    private val currentVersion: CurrentVersion
) : HGInterceptor, Interceptor {

    override fun intercept(chain: Interceptor.Chain) =
        with(chain) {
            val request = request().newBuilder()
                .addHeader("authorization", preferenceManager.accessToken)
                .addHeader("platform", "Android")
                .addHeader("version", currentVersion.version)
                .build()
            proceed(request)
        }
}