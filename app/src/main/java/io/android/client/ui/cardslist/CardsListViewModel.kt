/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.cardslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.android.client.api.repositories.MtgDataRepository
import io.android.client.api.responsemodels.CardsDataResponse
import io.android.client.localdata.CardDetailsExtras
import io.android.client.utils.MtgConstants
import io.android.client.utils.SingleLiveEvent
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CardsListViewModel @Inject constructor(
    private val mtgDataRepository: MtgDataRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var currentCardsData: LiveData<PagingData<CardsDataResponse.Card>>? = null

    //Booster Pack
    private var _boosterPackResponse: SingleLiveEvent<List<CardDetailsExtras>> = SingleLiveEvent()
    private val boosterPackResponse: LiveData<List<CardDetailsExtras>>?
       get() = _boosterPackResponse

    //private var boosterPack:  ResultWrapper<List<CardDetailsExtras>>? = null

    // Load Cards
    fun loadCardsData(setCode: String): LiveData<PagingData<CardsDataResponse.Card>> {

        // Check for previous loaded data
        val lastLoadedCards = currentCardsData
        if (lastLoadedCards != null) {
            return lastLoadedCards
        }

        val latestCardsData = mtgDataRepository.loadCards(setCode).cachedIn(viewModelScope)
        currentCardsData = latestCardsData

        return latestCardsData
    }

    // Generate Booster Pack
    suspend fun generateBooster(setCode: String):LiveData<List<CardDetailsExtras>>?{

        // Check for previous loaded data
        val lastLoadedCards = getLastSavedDataCopy()
        Timber.e("LAST LOADED DATA TOP $lastLoadedCards")

        if (lastLoadedCards != null) {
            Timber.e("LAST LOADED DATA $lastLoadedCards")
            _boosterPackResponse.postValue(lastLoadedCards!!)
            return boosterPackResponse
        }

      try {
        val latestBoosterCardsData = mtgDataRepository.generateBooster(setCode)
        var randomBoosterPack = arrayListOf<CardDetailsExtras>()
        latestBoosterCardsData.cards.forEach {
            randomBoosterPack.add(CardDetailsExtras(cardName = it.name,cardMultiverseId = it.multiverseid,cardImageUrl = it.imageUrl))
        }

        saveLocalCopy(randomBoosterPack)
        _boosterPackResponse.postValue(randomBoosterPack)
        return boosterPackResponse
        }
       catch (ex: Exception) {
            Timber.e("ERROR" + ex.message)
            return null
        }
    }

    private fun saveLocalCopy(value: List<CardDetailsExtras>?) {
            Timber.e("SAVING DATA $value")
            savedStateHandle.set(MtgConstants.SAVED_STATE_BOOSTER_PACK,value)
   }

    private fun getLastSavedDataCopy(): List<CardDetailsExtras>? {
        return savedStateHandle
            .getLiveData<List<CardDetailsExtras>>(
                MtgConstants.SAVED_STATE_BOOSTER_PACK
            ).value ?: null
    }
}
