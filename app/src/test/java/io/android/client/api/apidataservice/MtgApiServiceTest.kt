/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:58
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:29
 *
 */

package io.android.client.api.apidataservice

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class MtgApiServiceTest: ApiAbstract<MtgApiService>() {


    private lateinit var service: MtgApiService

    @Before
    fun initService() {
        service = createService(MtgApiService::class.java)
    }

    @Test
    fun setsDataPagedResponseSizeTest() {
        enqueueResponse("sets_response.json")
        runBlocking {
            val results = service.loadSets(10,1)
            val responseBody = requireNotNull(results.sets)
            mockWebServer.takeRequest()

            println("DATA LOADED" + responseBody[0].name)
            Truth.assertThat(responseBody.size).isEqualTo(10)
            Truth.assertThat(responseBody[0].name).isEqualTo("Tenth Edition")
        }
    }

    @Test
    fun cardDataLoadBySetCodeTest(){
        enqueueResponse("card_by_set_code_response.json")
        runBlocking {
            val results = service.loadCardsList(10,1,"10E")
            val responseBody = requireNotNull(results.cards)
            mockWebServer.takeRequest()

            println("DATA LOADED" + responseBody[0].set)
            Truth.assertThat(responseBody[0].set).isEqualTo("10E")
            Truth.assertThat(responseBody[0].name).isEqualTo("Ancestor's Chosen")
        }
    }

    @Test
    fun cardListPagedResponseSizeTest(){
        enqueueResponse("card_by_set_code_response.json")
        runBlocking {
            val results = service.loadCardsList(10,1,"10E")
            val responseBody = requireNotNull(results.cards)
            mockWebServer.takeRequest()

            println("DATA LOADED" + responseBody[0].set)
            Truth.assertThat(responseBody.size).isEqualTo(10)
        }
    }

    @Test
    fun boosterPackPagedListResponseSizeTest(){
        enqueueResponse("booster_pack_response.json")
        runBlocking {
            val results = service.generateBooster("10E")
            val responseBody = requireNotNull(results.cards)
            mockWebServer.takeRequest()

            println("DATA LOADED" + responseBody[0].set)
            Truth.assertThat(responseBody.size).isEqualTo(14)
        }
    }
}