/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.setslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.android.client.api.repositories.MtgDataRepository
import io.android.client.api.responsemodels.SetsDataResponse
import javax.inject.Inject

@HiltViewModel
class SetsListViewModel @Inject constructor(
    private val mtgDataRepository: MtgDataRepository
) : ViewModel() {

    private var currentSets: LiveData<PagingData<SetsDataResponse.Set>>? = null

    fun loadSetsData(): LiveData<PagingData<SetsDataResponse.Set>> {

        // Check for previous loaded data, prevent reloading of data unnecessarily.
        val lastLoadedSets = currentSets
        if (lastLoadedSets != null) {
            return lastLoadedSets
        }

        // Cache the data in ViewModel Scope
        val latestSetsData = mtgDataRepository.loadSets().cachedIn(viewModelScope)
        currentSets = latestSetsData
        return latestSetsData
    }
}
