/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.api.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetsDataResponse(
    @SerializedName("sets")
    val sets: List<Set>
) {
    data class Set(
        @Expose
        @SerializedName("block")
        val block: String,
        @Expose
        @Transient
        @SerializedName("booster")
        val booster: List<Array<String>>,
        @Expose
        @SerializedName("code")
        val code: String,
        @Expose
        @SerializedName("name")
        val name: String,
        @Expose
        @SerializedName("onlineOnly")
        val onlineOnly: Boolean,
        @Expose
        @SerializedName("releaseDate")
        val releaseDate: String,
        @Expose
        @SerializedName("type")
        val type: String
    )
}
