/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 */

package io.android.client.ui.setslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.android.client.R
import io.android.client.api.apidataservice.RetrofitClient
import io.android.client.api.repositories.MtgDataRepository
import io.android.client.api.responsemodels.SetsDataResponse
import io.android.client.databinding.FragmentListBinding
import io.android.client.localdata.SetDetailsExtras
import io.android.client.ui.MainActivity
import io.android.client.ui.base.BaseFragment
import io.android.client.utils.MtgConstants
import io.android.client.utils.PagingLoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SetsListFragment : BaseFragment<FragmentListBinding>(), SetsListAdapter.SetClickListener {

    // This property is only valid between onCreateView and
    // onDestroyView.
    override var binding: FragmentListBinding? = null
    private var loadSetsJob: Job? = null
    //private val viewModel: SetsListViewModel by viewModels()
    private lateinit var setsListAdapter: SetsListAdapter

    //manual without di
    private lateinit var viewModel: SetsListViewModel
    private lateinit var setsListViewModelFactory: SetsListViewModelFactory
    private lateinit var repository: MtgDataRepository

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //INIT MANUALLY
        repository = MtgDataRepository(RetrofitClient.apiService)
        setsListViewModelFactory = SetsListViewModelFactory(repository)
        viewModel = setsListViewModelFactory.create(SetsListViewModel::class.java)

        // Update toolbar title directly from fragment
        (requireActivity() as MainActivity).title = getString(R.string.first_fragment_label)
        initAdapter()
        initLoadData()
    }

    private fun initLoadData() {
        loadSetsJob?.cancel()
        loadSetsJob = lifecycleScope.launch {

            viewModel.loadSetsData().observe(
                viewLifecycleOwner,
                {
                    setsListAdapter.submitData(lifecycle, it)
                }
            )
        }
    }

    private fun initAdapter() {
        setsListAdapter = SetsListAdapter(this)

        setsListAdapter.addLoadStateListener { loadState ->
            binding?.setsRecyclerView?.isVisible = loadState.refresh is LoadState.NotLoading
            binding?.progress?.isVisible = loadState.refresh is LoadState.Loading
            binding?.refreshingLayout?.isRefreshing = loadState.refresh is LoadState.Loading
            manageErrors(loadState)
        }

        binding?.setsRecyclerView?.setHasFixedSize(true)
        binding?.setsRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding?.setsRecyclerView?.adapter = setsListAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(setsListAdapter),
            footer = PagingLoadStateAdapter(setsListAdapter)
        )

        binding?.refreshingLayout?.setOnRefreshListener { setsListAdapter.refresh() }
    }

    private fun manageErrors(loadState: CombinedLoadStates) {
        binding?.layoutNoData?.emptyView?.isVisible = loadState.refresh is LoadState.Error
        binding?.layoutNoData?.textRetryLoadButton?.setOnClickListener { setsListAdapter.retry() }
    }

    override fun onCharacterClicked(
        setDataItem: SetsDataResponse.Set
    ) {
        renderOptionsDialog(setDataItem)
    }

    private fun renderOptionsDialog(setDataItem: SetsDataResponse.Set) {
        val list = listOf(getString(R.string.option_view_cards_list), getString(R.string.option_generate_random_booster))
        val adapter = ArrayAdapter<CharSequence>(requireContext(), R.layout.item_dialog_textview, list)
        var selectedOption: Int? = null
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle(getString(R.string.select_option_message))
        ad.setSingleChoiceItems(adapter, -1) { dialog, which ->
            selectedOption = which
        }
        ad.setPositiveButton(
            getString(R.string.ok_message)
        ) { p0, p1 ->
            if (selectedOption == 0) {
                p0.dismiss()
                val setDetailsExtras = SetDetailsExtras(
                    setName = setDataItem.name, setCode = setDataItem.code,
                    optionType = MtgConstants.LIST_CARDS_TYPE
                )
                navigateToCards(setDetailsExtras)
            } else if (selectedOption == 1) {
                p0.dismiss()
                val setDetailsExtras = SetDetailsExtras(
                    setName = setDataItem.name, setCode = setDataItem.code,
                    optionType = MtgConstants.GENERATE_BOOSTER_TYPE
                )
                navigateToCards(setDetailsExtras)
            }
        }
        ad.setNegativeButton(
            getString(R.string.cancel_option)
        ) { p0, p1 -> p0?.dismiss() }
        ad.create().show()
    }

    private fun navigateToCards(detailsExtras: SetDetailsExtras) {
        findNavController().navigate(
            SetsListFragmentDirections.actionFirstFragmentToCardsListFragment(detailsExtras)
        )
    }
}
