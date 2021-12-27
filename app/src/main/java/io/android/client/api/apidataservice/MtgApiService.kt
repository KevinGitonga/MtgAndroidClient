/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.api.apidataservice

import io.android.client.api.responsemodels.CardDetailsResponse
import io.android.client.api.responsemodels.CardsDataResponse
import io.android.client.api.responsemodels.SetsDataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points. Will interact with MagicTheGathering Restful APIS.
 */
interface MtgApiService {

    /**
     * Get all Magic: The Gathering sets.
     *
     *  @param pageSize
     *  @param page
     * @return Returns a list of 10 sets for each call.
     */
    @GET("sets")
    suspend fun loadSets(@Query("pageSize") pageSize: Int, @Query("page") pageNumber: Int): SetsDataResponse

    /**
     * Get all Magic: The Gathering cards.
     *
     * @param pageSize The page size.
     * @param page The next page.
     * @param set The setcode for set
     * @return Returns a list containing all of the Magic: The Gathering cards.
     */
    @GET("cards")
    suspend fun loadCardsList(@Query("pageSize") pageSize: Int, @Query("page") pageNumber: Int,@Query("set") setCode: String): CardsDataResponse

    /**
     * Generates a booster pack by a specific set code.
     *
     * @param setCode The set code. For example '10E' (Tenth Edition)
     * @param pageSize The page size.
     * @param page The next page.
     * @return Returns a booster pack with 10 random cards.
     */
    @GET("sets/{id}/booster")
    suspend fun generateBooster(@Path("id") setCode: String): CardsDataResponse

    /**
     * Get a specific Magic: The Gathering card.
     *
     * @param multiverseId The official Magic: The Gathering card id.
     * @return Returns a specific Magic: The Gathering card.
     */
    @GET("cards/{id}")
    suspend fun loadCardDetails(@Path("id") cardMultiverseId: String): CardDetailsResponse
}
