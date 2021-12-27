/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.android.client.BuildConfig
import io.android.client.api.apidataservice.MtgApiService
import io.android.client.api.repositories.MtgDataRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *  Api Module
 *
 *  Provides functionality to access below utility methods without instantiating them manually
 *  Most are Singleton pattern to ensure they are created once and reused
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = when (BuildConfig.BUILD_TYPE) {
            "release" -> {
                HttpLoggingInterceptor.Level.NONE
            }
            else -> {
                HttpLoggingInterceptor.Level.BODY
            }
        }

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMtgApiService(retrofit: Retrofit): MtgApiService {
        return retrofit.create(MtgApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataRepository(dataApiService: MtgApiService): MtgDataRepository {
        Timber.e("BINDING data REPO}")
        return MtgDataRepository(dataApiService)
    }
}
