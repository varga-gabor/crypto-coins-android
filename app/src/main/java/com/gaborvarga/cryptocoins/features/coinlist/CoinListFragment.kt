package com.aldi.cryptocoins.features.coinlist

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.features.coinlist.model.CoinListUiState
import com.aldi.cryptocoins.features.coinlist.recyclerview.CoinListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinListFragment : Fragment(R.layout.fragment_coinlist) {

    private val viewModel by viewModel<CoinListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uiComponents = initUiComponents(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onViewStarted()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    render(uiComponents, uiState)
                }
            }
        }
    }

    private fun initUiComponents(view: View): UiComponents {
        val recyclerViewAdapter = CoinListAdapter().apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.coinsRecyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerViewAdapter
        }
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        return UiComponents(
            recyclerViewAdapter,
            recyclerView,
            progressBar,
        )
    }

    private fun render(uiComponents: UiComponents, uiState: CoinListUiState) {
        uiComponents.apply {
            recyclerViewAdapter.items = uiState.coinList
            recyclerView.isVisible = !uiState.isLoading
            progressBar.isVisible = uiState.isLoading
        }
    }

    private data class UiComponents(
        val recyclerViewAdapter: CoinListAdapter,
        val recyclerView: RecyclerView,
        val progressBar: ProgressBar,
    )
}
