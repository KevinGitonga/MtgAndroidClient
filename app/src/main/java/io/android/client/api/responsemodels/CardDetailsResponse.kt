/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.api.responsemodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDetailsResponse(
    @SerializedName("card")
    val card: Card
):Parcelable {
    @Parcelize
    data class Card(
        @SerializedName("artist")
        val artist: String,
        @SerializedName("cmc")
        val cmc: Double,
        @SerializedName("colorIdentity")
        val colorIdentity: List<String>,
        @SerializedName("colors")
        val colors: List<String>,
        @SerializedName("foreignNames")
        val foreignNames: List<ForeignName>,
        @SerializedName("id")
        val id: String,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("layout")
        val layout: String,
        @SerializedName("legalities")
        val legalities: List<Legality>,
        @SerializedName("manaCost")
        val manaCost: String,
        @SerializedName("multiverseid")
        val multiverseid: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("number")
        val number: String,
        @SerializedName("originalText")
        val originalText: String,
        @SerializedName("originalType")
        val originalType: String,
        @SerializedName("power")
        val power: String,
        @SerializedName("printings")
        val printings: List<String>,
        @SerializedName("rarity")
        val rarity: String,
        @SerializedName("set")
        val `set`: String,
        @SerializedName("setName")
        val setName: String,
        @SerializedName("subtypes")
        val subtypes: List<String>,
        @SerializedName("text")
        val text: String,
        @SerializedName("toughness")
        val toughness: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("types")
        val types: List<String>,
        @SerializedName("variations")
        val variations: List<String>
    ):Parcelable {
        @Parcelize
        data class ForeignName(
            @SerializedName("flavor")
            val flavor: String,
            @SerializedName("imageUrl")
            val imageUrl: String,
            @SerializedName("language")
            val language: String,
            @SerializedName("multiverseid")
            val multiverseid: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("text")
            val text: String,
            @SerializedName("type")
            val type: String
        ):Parcelable

        @Parcelize
        data class Legality(
            @SerializedName("format")
            val format: String,
            @SerializedName("legality")
            val legality: String
        ):Parcelable
    }
}
