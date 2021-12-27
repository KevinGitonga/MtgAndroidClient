/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.carddetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.android.client.api.repositories.MtgDataRepository
import io.android.client.api.responsemodels.CardDetailsResponse
import io.android.client.api.responsemodels.ResultWrapper
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CardsDetailsViewModel @Inject constructor(private val mtgDataRepository: MtgDataRepository) : ViewModel() {

    private var _detailsDataResponse: MutableLiveData<ResultWrapper<CardDetailsResponse>> = MutableLiveData()
    val detailsDataResponse: LiveData<ResultWrapper<CardDetailsResponse>>
        get() = _detailsDataResponse

    // Load Cards
    suspend fun loadCardsDetails(cardMultiverseId: String) {
        _detailsDataResponse.postValue(ResultWrapper.Loading)
        try {
            val detailsResponse = mtgDataRepository.loadCardDetails(cardMultiverseId)
            _detailsDataResponse.postValue(ResultWrapper.Success(detailsResponse))
        } catch (ex: Exception) {
            _detailsDataResponse.postValue(ResultWrapper.Error(exception = ex.message.toString()))
        }
    }
}
