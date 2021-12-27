/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.localdata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  SetDetailsExtras
 *
 *  Parcelable data class for sharing data from SetListFragment to CardListFragment
 */
@Parcelize
data class SetDetailsExtras(
    val setName: String,
    val setCode: String,
    val optionType: Int
) : Parcelable
