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
 *  CardDetailsExtras
 *
 *  Parcelable data class for sharing data from CardListFragment to CardDetailsFragment
 */
@Parcelize
data class CardDetailsExtras(val cardName: String?, val cardMultiverseId: String?, val cardImageUrl:String?) : Parcelable
