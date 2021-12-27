/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.api.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import io.android.client.api.apidataservice.MtgApiService
import io.android.client.api.pageddatasource.CardsPagingSource
import io.android.client.api.pageddatasource.MtgPagingSource
import io.android.client.api.responsemodels.CardDetailsResponse
import io.android.client.api.responsemodels.CardsDataResponse
import io.android.client.api.responsemodels.SetsDataResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  MtgDataRepository
 *
 *  Repository to interact with data sources as recommended by Google.
 */
@Singleton
class MtgDataRepository @Inject constructor(private val dataApiService: MtgApiService) {

    // Load sets
    fun loadSets(): LiveData<PagingData<SetsDataResponse.Set>> {

        return Pager(
            pagingSourceFactory = { MtgPagingSource(dataApiService) },
            config = PagingConfig(
                pageSize = 10, prefetchDistance = 1
            )
        ).liveData
    }

    // Load cards
    fun loadCards(setCode: String): LiveData<PagingData<CardsDataResponse.Card>> {
        return Pager(
            pagingSourceFactory = { CardsPagingSource(dataApiService, setCode) },
            config = PagingConfig(
                pageSize = 10, prefetchDistance = 1
            )
        ).liveData
    }

    // Generate Booster Pack
    suspend fun generateBooster(setCode: String): CardsDataResponse {
        return dataApiService.generateBooster(setCode)
    }

    // Load card details
    suspend fun loadCardDetails(cardMultiverseId: String): CardDetailsResponse {
        return dataApiService.loadCardDetails(cardMultiverseId = cardMultiverseId)
    }
}

