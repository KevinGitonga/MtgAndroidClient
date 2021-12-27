/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:58
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:29
 *
 */

package io.android.client.api.apidataservice

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import io.android.client.utils.loadJsonFile
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@RunWith(JUnit4::class)
abstract class ApiAbstract<T> {

    lateinit var mockWebServer: MockWebServer

    @Throws(IOException::class)
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Throws(IOException::class)
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Throws(IOException::class)
    fun enqueueResponse(fileName: String) {
        enqueueResponse(fileName, emptyMap() )
    }

    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(loadJsonFile("json/$fileName")))
    }

    fun createService(clazz: Class<T>): T {

        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(clazz)
    }
}