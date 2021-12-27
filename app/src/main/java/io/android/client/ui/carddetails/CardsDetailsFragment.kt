/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.carddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import io.android.client.R
import io.android.client.api.responsemodels.CardDetailsResponse
import io.android.client.api.responsemodels.ResultWrapper
import io.android.client.databinding.FragmentCardDetailsBinding
import io.android.client.ui.MainActivity
import io.android.client.ui.base.BaseFragment
import io.android.client.utils.NetworkUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class CardsDetailsFragment : BaseFragment<FragmentCardDetailsBinding>() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    override var binding: FragmentCardDetailsBinding? = null
    private var loadSetsJob: Job? = null
    private val viewModel: CardsDetailsViewModel by viewModels()
    private val args: CardsDetailsFragmentArgs by navArgs()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCardDetailsBinding {
        return FragmentCardDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update toolbar title directly from fragment
        (requireActivity() as MainActivity).title = args.carddetails.cardName

        if (NetworkUtils.isOnline(requireContext())){
            initLoadData()
        }
        else{
            Toast.makeText(requireContext(),getString(R.string.network_connection_message),Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLoadData() {
        loadSetsJob?.cancel()
        loadSetsJob = lifecycleScope.launch {
            viewModel.loadCardsDetails(args.carddetails.cardMultiverseId.toString())
            viewModel.detailsDataResponse.observe(viewLifecycleOwner, {
                when (it) {
                    is ResultWrapper.Loading -> {
                        binding?.progress?.visibility = View.VISIBLE
                    }
                    is ResultWrapper.Success -> {
                        Timber.e(it.data.card.name)
                        binding?.progress?.visibility = View.GONE
                        binding?.mainConstraint?.visibility = View.VISIBLE
                        bindDataToUi(it.data)
                    }
                    is ResultWrapper.Error -> {
                        Timber.e(it.exception)
                        Toast.makeText(requireContext(), it.exception, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun bindDataToUi(data: CardDetailsResponse) {
        binding?.cardImage?.load(data.card.imageUrl) {
            placeholder(R.drawable.image_dummy_place_holder)
        }

        binding?.tvName?.text = "Name: ${data.card.name ?: context?.getString(R.string.no_data_place_holder)}"
        binding?.tvManaCost?.text = "Mana Cost: ${data.card.manaCost ?: context?.getString(R.string.no_data_place_holder)}"
        binding?.tvCmc?.text = "Cmc: ${data.card.cmc.toInt() ?: context?.getString(R.string.no_data_place_holder)}"
        binding?.tvCardType?.text = "Original type: ${data.card.originalType ?: context?.getString(R.string.no_data_place_holder)}"
        binding?.aboutText?.text = "About text: ${data.card.text ?: context?.getString(R.string.no_data_place_holder)}"

        if (!data.card.colors.isNullOrEmpty()) {
            binding?.colorsText?.text = "Colors: ${data.card.colors[0] ?: context?.getString(R.string.no_data_place_holder)}"
        }

        binding?.setNameText?.text = "Set name: ${data.card.setName ?: context?.getString(R.string.no_data_place_holder)}"
        binding?.rarityText?.text = "Rarity: ${data.card.rarity ?: context?.getString(R.string.no_data_place_holder)}"
        binding?.artistName?.text = "Artist: ${data.card.artist ?: context?.getString(R.string.no_data_place_holder)}"
    }

}
