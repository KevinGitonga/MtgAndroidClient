/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.setslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.android.client.api.repositories.MtgDataRepository

class SetsListViewModelFactory(private val mtgDataRepository: MtgDataRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SetsListViewModel(mtgDataRepository) as T
    }
}