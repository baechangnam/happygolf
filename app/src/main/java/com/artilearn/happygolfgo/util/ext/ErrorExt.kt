package com.artilearn.happygolfgo.util.ext

import com.artilearn.happygolfgo.data.ApiError
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException


fun errorLog(tag: String, t: Throwable) {
    when (t) {
        is HttpException -> {
            if (t.code() != 419) {
                val errorBody = t.response()?.errorBody()
                val gson = Gson()
                val type = object : TypeToken<ApiError>() {}.type
                val errorResponse: ApiError = gson.fromJson(errorBody?.charStream(), type)
                crashlyticsLog(tag, t, errorResponse)
            }
        }
        else -> crashlyticsLog(tag, t)
    }
}

private fun crashlyticsLog(tag: String, t: Throwable, error: ApiError? = null) {
    try {
        throw IllegalStateException(t)
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
        FirebaseCrashlytics.getInstance().log("t: $tag, $t, ${t.localizedMessage}")
        FirebaseCrashlytics.getInstance().log("error: $tag, ${error?.statusCode}, ${error?.resultCode}, ${error?.message}")
    }
}