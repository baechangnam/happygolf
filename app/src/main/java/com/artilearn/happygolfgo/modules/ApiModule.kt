package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.BuildConfig
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.api.LectureApiInterface
import com.artilearn.happygolfgo.util.CurrentVersion
import com.artilearn.happygolfgo.util.PreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        Interceptor { chain ->
            with(chain) {
                val request = request().newBuilder()
                    .addHeader("authorization", get<PreferenceManager>().accessToken)
                    .addHeader("platform", "Android")
                    .addHeader("version", get<CurrentVersion>().version)
                    .build()
                proceed(request)
            }
        }
    }

    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(get<Interceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    single<GsonConverterFactory> { GsonConverterFactory.create() }

    single<RxJava2CallAdapterFactory> { RxJava2CallAdapterFactory.create() }

    single<ApiInterface> { get<Retrofit>().create(ApiInterface::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            //.baseUrl(get<PreferenceManager>().environmentMode)
            .baseUrl(BuildConfig.HG_BASE_URL)
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
    }

    single<LectureApiInterface> {
        var retrofit =  Retrofit.Builder()
            .baseUrl(BuildConfig.TRAINING_BASE_URL)
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())

            .build()

        retrofit.create(LectureApiInterface::class.java)
    }


}