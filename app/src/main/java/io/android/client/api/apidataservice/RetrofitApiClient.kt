/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.api.apidataservice

import com.google.gson.GsonBuilder
import io.android.client.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient{

        //service instance
        val apiService: MtgApiService by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
           getRetrofit()
                .create(MtgApiService::class.java)
        }


        private fun getRetrofit(): Retrofit {
            val gsonParser= GsonBuilder().setLenient().create()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)  //Configure yourself
                .client(getOkNormalClient())
                .addConverterFactory(GsonConverterFactory.create(gsonParser))
                .build()

        }


        private fun getOkNormalClient(): OkHttpClient {
            //Add a log interceptor, print alllog
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            //Can set the level of request filtering,body,basic,headers
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


            return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor) //Logs, all request responsiveness seen
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
        }
}