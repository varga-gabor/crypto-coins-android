package com.aldi.cryptocoins.features.coinlist.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry

class CoinListDiffUtil : DiffUtil.ItemCallback<CoinListEntry>() {

    override fun areItemsTheSame(oldItem: CoinListEntry, newItem: CoinListEntry): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CoinListEntry, newItem: CoinListEntry): Boolean =
        oldItem.id == newItem.id &&
        oldItem.name == newItem.name &&
        oldItem.symbol == newItem.symbol &&
        oldItem.price == newItem.price &&
        oldItem.changePercent == newItem.changePercent &&
        oldItem.changePercentColor == newItem.changePercentColor &&
        oldItem.icon == newItem.icon

}
