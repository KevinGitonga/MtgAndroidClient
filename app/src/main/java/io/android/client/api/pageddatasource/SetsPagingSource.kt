/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.api.pageddatasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.android.client.api.apidataservice.MtgApiService
import io.android.client.api.responsemodels.SetsDataResponse
import io.android.client.utils.MtgConstants.Companion.INITIAL_PAGE
import timber.log.Timber

/**
 *  MtgPagingSource
 *
 *  Paging data source to interact with MtgApiInterface and provide functionality to load more data as user Navigates downwards on Recyclerview.
 */
class MtgPagingSource(
    private val api: MtgApiService
) : PagingSource<Int, SetsDataResponse.Set>() {
    override fun getRefreshKey(state: PagingState<Int, SetsDataResponse.Set>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SetsDataResponse.Set> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = api.loadSets(params.loadSize, page)

            Timber.e("DATA RESPONSE ${response?.sets}")
            LoadResult.Page(
                data = response?.sets,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response?.sets?.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Timber.e("ERROR RESPONSE" + e.message)
            LoadResult.Error(e)
        }
    }
}
