package com.artilearn.happygolfgo.data.remote.retrofit

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.util.PreferenceManager
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi(private val prefs: PreferenceManager) {

    private lateinit var api: ApiInterface

    fun getInstance(): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(prefs.environmentMode)
            //.client(get)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        api = retrofit.create(ApiInterface::class.java)

        return api
    }
}