/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.cardslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.android.client.R
import io.android.client.api.responsemodels.CardsDataResponse
import io.android.client.databinding.FragmentListBinding
import io.android.client.localdata.CardDetailsExtras
import io.android.client.ui.MainActivity
import io.android.client.ui.base.BaseFragment
import io.android.client.utils.MtgConstants
import io.android.client.utils.NetworkUtils
import io.android.client.utils.PagingLoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class CardsListFragment : BaseFragment<FragmentListBinding>(), CardsListAdapter.CardClickListener,BoosterPackAdapter.CardClickListener {

    // This property is only valid between onCreateView and
    // onDestroyView.
    override var binding: FragmentListBinding? = null
    private var loadSetsJob: Job? = null
    private val viewModel: CardsListViewModel by viewModels()
    private lateinit var setsListAdapter: CardsListAdapter
    private var boosterPackAdapter : BoosterPackAdapter? = null
    private val args: CardsListFragmentArgs by navArgs()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Init refresh
        initRefreshListener()

        //Load data based on use input on Dialog
        //LIST CARDS STATEMENT
        if (args.setdetails.optionType == MtgConstants.LIST_CARDS_TYPE) {
            initAdapter()
            (requireActivity() as MainActivity).title = getString(R.string.cards_list_title)
            initLoadCards()
        }
        //GENERATE BOOSTER STATEMENT
        else if (args.setdetails.optionType == MtgConstants.GENERATE_BOOSTER_TYPE) {
            (requireActivity() as MainActivity).title = getString(R.string.booster_pack_title)
            //CHECK NETWORK CONNECTION BEFORE MAKING CALLS
            if (NetworkUtils.isOnline(requireContext())){
                initGenerateBooster()
            }
            else{
                showErrorView(true)
            }

        }
    }

    private fun initRefreshListener() {
        if (args.setdetails.optionType == MtgConstants.LIST_CARDS_TYPE) {
            binding?.refreshingLayout?.setOnRefreshListener {
                setsListAdapter.refresh()

            }
        }
        else if (args.setdetails.optionType == MtgConstants.GENERATE_BOOSTER_TYPE) {
            binding?.refreshingLayout?.setOnRefreshListener {
                if (NetworkUtils.isOnline(requireContext())){
                    initGenerateBooster()
                }
                else{
                    showErrorView(true)
                }
            }
        }

    }

    private fun showErrorView(showViews:Boolean) {
        binding?.layoutNoData?.emptyView?.isVisible = showViews
        binding?.layoutNoData?.textRetryLoadButton?.setOnClickListener {
            if (NetworkUtils.isOnline(requireContext())){
                initGenerateBooster()
            }
            else{
                showErrorView(true)
            }
        }
    }

    private fun initGenerateBooster() {


        loadSetsJob?.cancel()
        loadSetsJob = lifecycleScope.launch {

            //show loading
            binding?.progress?.isVisible = true
            binding?.refreshingLayout?.isRefreshing = true
            binding?.setsRecyclerView?.isVisible = false
            binding?.refreshingLayout?.isRefreshing = true

            //Observer for data updates from viewmodel
            viewModel.generateBooster(args.setdetails.setCode)?.observe(
                viewLifecycleOwner,
                {
                    //cancel loading show data
                    binding?.progress?.isVisible = false
                    binding?.refreshingLayout?.isRefreshing = false
                    binding?.setsRecyclerView?.visibility = View.VISIBLE
                    binding?.setsRecyclerView?.isVisible = true
                    bindDataToUi(it)
                }
            )
        }
    }

    private fun bindDataToUi(data: List<CardDetailsExtras>) {
        boosterPackAdapter = BoosterPackAdapter(this,data)
        binding?.setsRecyclerView?.setHasFixedSize(true)
        binding?.setsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        binding?.setsRecyclerView?.adapter = boosterPackAdapter
    }

    private fun initLoadCards() {
        loadSetsJob?.cancel()
        loadSetsJob = lifecycleScope.launch {

            Timber.e("SETCODE CARDS" + args.setdetails.setCode)
            //Observer for data updates from viewmodel
            viewModel.loadCardsData(args.setdetails.setCode).observe(
                viewLifecycleOwner,
                {
                    setsListAdapter.submitData(lifecycle, it)
                }
            )
        }
    }

    private fun initAdapter() {
        setsListAdapter = CardsListAdapter(this)

        //Change UI STATUS BASED ON PAGING API PROVIDED STATE
        setsListAdapter.addLoadStateListener { loadState ->
            binding?.setsRecyclerView?.isVisible = loadState.refresh is LoadState.NotLoading
            binding?.progress?.isVisible = loadState.refresh is LoadState.Loading
            binding?.refreshingLayout?.isRefreshing = loadState.refresh is LoadState.Loading
            manageErrors(loadState)
        }

        binding?.setsRecyclerView?.setHasFixedSize(true)
        binding?.setsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)

        //ADD LOADER ON PAGING ADAPTER BOTTOM
        binding?.setsRecyclerView?.adapter = setsListAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(setsListAdapter),
            footer = PagingLoadStateAdapter(setsListAdapter)
        )
    }

    private fun manageErrors(loadState: CombinedLoadStates) {
        //UPDATE UI INCASE OF ERRORS FROM PAGING API
        binding?.layoutNoData?.emptyView?.isVisible = loadState.refresh is LoadState.Error
        binding?.layoutNoData?.textRetryLoadButton?.setOnClickListener { setsListAdapter.retry() }
    }

    override fun onCardClicked(card: CardsDataResponse.Card) {
        navigateToDetails(card)
    }

    private fun navigateToDetails(card: CardsDataResponse.Card) {
        val cardDetailsExtra = CardDetailsExtras(card.name, card.multiverseid,card.imageUrl)
        findNavController().navigate(
            CardsListFragmentDirections.actionCardsListFragmentToCardsDetailsFragment(cardDetailsExtra)
        )
    }

    private fun navigateToDetails(card: CardDetailsExtras) {
        val cardDetailsExtra = CardDetailsExtras(card.cardName, card.cardMultiverseId,card.cardImageUrl)
        findNavController().navigate(
            CardsListFragmentDirections.actionCardsListFragmentToCardsDetailsFragment(cardDetailsExtra)
        )
    }

    override fun onCardClicked(card: CardDetailsExtras) {
        navigateToDetails(card)
    }
}




