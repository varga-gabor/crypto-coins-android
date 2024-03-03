package com.aldi.cryptocoins.features.coinlist.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry
import com.aldi.cryptocoins.features.coinlist.recyclerview.CoinListAdapter.CoinViewHolder

@SuppressLint("NotifyDataSetChanged")
class CoinListAdapter : ListAdapter<CoinListEntry, CoinViewHolder>(CoinListDiffUtil()) {

    var items: List<CoinListEntry> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClickedListener: ((coinId: String) -> Unit)? = null

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_coinlist_item, parent, false)

        return CoinViewHolder(view, onItemClickedListener)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class CoinViewHolder(
        private val view: View,
        private val onItemClickedListener: ((coinId: String) -> Unit)?,
    ) : RecyclerView.ViewHolder(view) {

        private val coinImageView = view.findViewById<ImageView>(R.id.coinImageView)
        private val coinNameTextView = view.findViewById<TextView>(R.id.coinNameTextView)
        private val coinSymbolTextView = view.findViewById<TextView>(R.id.coinSymbolTextView)
        private val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
        private val changePercentTextView = view.findViewById<TextView>(R.id.changePercentTextView)

        fun bind(coinListEntry: CoinListEntry) {
            coinImageView.setImageResource(coinListEntry.icon)
            coinNameTextView.text = coinListEntry.name
            coinSymbolTextView.text = coinListEntry.symbol
            priceTextView.text = coinListEntry.price
            changePercentTextView.apply {
                text = coinListEntry.changePercent
                setTextColor(ContextCompat.getColor(context, coinListEntry.changePercentColor))
            }
            view.setOnClickListener {
                onItemClickedListener?.invoke(coinListEntry.id)
            }
        }
    }
}
