/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.setslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.android.client.R
import io.android.client.api.responsemodels.SetsDataResponse
import io.android.client.databinding.ItemSetDataBinding

class SetsListAdapter(private val setClickListener: SetClickListener) : PagingDataAdapter<SetsDataResponse.Set, SetsListAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemSetDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: ItemSetDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(singleSet: SetsDataResponse.Set) = with(binding) {
            val setName = "Set name: ${singleSet.name ?: root.context.getString(R.string.no_data_place_holder)}"
            val releaseDate = "Release date: ${singleSet.releaseDate ?: root.context.getString(R.string.no_data_place_holder) }"
            val blockTypeName = "Block name: ${singleSet.block ?: root.context.getString(R.string.no_data_place_holder)}"
            val setTypeName = "Set code: ${singleSet.code ?: root.context.getString(R.string.no_data_place_holder)}"
            when {
                !setName.isNullOrEmpty() -> {
                    tvSetName.text = setName
                }
                else -> {
                    tvSetName.visibility = View.GONE
                }
            }

            when {
                !releaseDate.isNullOrEmpty() -> {
                    tvReleaseDate.text = releaseDate
                }
                else -> {
                    tvReleaseDate.visibility = View.GONE
                }
            }

            when {
                !blockTypeName.isNullOrEmpty() -> {
                    setBlockType.text = blockTypeName
                }
                else -> {
                    setBlockType.visibility = View.GONE
                }
            }

            when {
                !setTypeName.isNullOrEmpty() -> {
                    setType.text = setTypeName
                }
                else -> {
                    setType.visibility = View.GONE
                }
            }

            itemView.setOnClickListener {
                setClickListener?.onCharacterClicked(
                    singleSet
                )
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SetsDataResponse.Set>() {
            override fun areItemsTheSame(oldItem: SetsDataResponse.Set, newItem: SetsDataResponse.Set): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: SetsDataResponse.Set, newItem: SetsDataResponse.Set): Boolean =
                oldItem == newItem
        }
    }

    interface SetClickListener {
        fun onCharacterClicked(setDataItem: SetsDataResponse.Set)
    }
}
