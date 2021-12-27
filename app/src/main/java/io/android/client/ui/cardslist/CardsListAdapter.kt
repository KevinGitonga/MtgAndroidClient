/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.cardslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import io.android.client.R
import io.android.client.api.responsemodels.CardsDataResponse
import io.android.client.databinding.ItemCardLayoutBinding

class CardsListAdapter(private val setClickListener: CardClickListener) : PagingDataAdapter<CardsDataResponse.Card, CardsListAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: ItemCardLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: CardsDataResponse.Card) = with(binding) {

            binding.ivCardImage.load(card.imageUrl) {
                listener(
                    onSuccess = { _, _ ->
                        // do something
                        binding.progress.visibility = View.GONE
                    }, onError = { _: ImageRequest, throwable: Throwable ->
                    // handle error here
                    binding.progress.visibility = View.GONE
                    binding.ivCardImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    binding.ivCardImage.load(R.drawable.error_place_holder)
                }, onStart = {
                    binding.progress.visibility = View.VISIBLE
                }

                ).placeholder(R.drawable.image_dummy_place_holder)
            }

            itemView.setOnClickListener {
                setClickListener?.onCardClicked(
                    card
                )
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CardsDataResponse.Card>() {
            override fun areItemsTheSame(oldItem: CardsDataResponse.Card, newItem: CardsDataResponse.Card): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CardsDataResponse.Card, newItem: CardsDataResponse.Card): Boolean =
                oldItem == newItem
        }
    }

    interface CardClickListener {
        fun onCardClicked(card: CardsDataResponse.Card)
    }
}
