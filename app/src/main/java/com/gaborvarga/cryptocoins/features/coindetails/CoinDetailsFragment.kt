package com.aldi.cryptocoins.features.coindetails

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.features.coindetails.model.CoinDetailsUiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinDetailsFragment : Fragment(R.layout.fragment_coindetails) {

    private val viewModel by viewModel<CoinDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uiComponents = initUiComponents(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    render(uiComponents, uiState)
                }
            }
        }
    }

    private fun initUiComponents(view: View): UiComponents {
        val toolbarTitleTextView = view.findViewById<TextView>(R.id.toolbarTitleTextView)
        val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
        val changePercentTextView = view.findViewById<TextView>(R.id.changePercentTextView)
        val marketCapTextView = view.findViewById<TextView>(R.id.marketCapTextView)
        val volumeTextView = view.findViewById<TextView>(R.id.volumeTextView)
        val supplyTextView = view.findViewById<TextView>(R.id.supplyTextView)
        val loadingOverlayGroup = view.findViewById<Group>(R.id.loadingOverlayGroup)

        view.findViewById<ImageView>(R.id.backArrowImageView).apply {
            setOnClickListener {}
        }

        return UiComponents(
            toolbarTitleTextView,
            priceTextView,
            changePercentTextView,
            marketCapTextView,
            volumeTextView,
            supplyTextView,
            loadingOverlayGroup,
        )
    }

    private fun render(uiComponents: UiComponents, uiState: CoinDetailsUiState) {
        uiComponents.apply {
            toolbarTitleTextView.text = uiState.coinDetails.name
            priceTextView.text = uiState.coinDetails.price
            changePercentTextView.apply {
                text = uiState.coinDetails.changePercent
                setTextColor(
                    ContextCompat.getColor(context, uiState.coinDetails.changePercentColor)
                )
            }
            marketCapTextView.text = uiState.coinDetails.marketCap
            volumeTextView.text = uiState.coinDetails.volume
            supplyTextView.text = uiState.coinDetails.supply
            loadingOverlayGroup.isVisible = uiState.isLoading
        }
    }

    private data class UiComponents(
        val toolbarTitleTextView: TextView,
        val priceTextView: TextView,
        val changePercentTextView: TextView,
        val marketCapTextView: TextView,
        val volumeTextView: TextView,
        val supplyTextView: TextView,
        val loadingOverlayGroup: Group,
    )
}
