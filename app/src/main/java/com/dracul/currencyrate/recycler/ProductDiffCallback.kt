package com.dracul.currencyrate.recycler

import androidx.recyclerview.widget.DiffUtil
import com.dracul.currencyrate.data.Valute

class ProductDiffCallback: DiffUtil.ItemCallback<Valute>() {
    override fun areItemsTheSame(oldItem: Valute, newItem: Valute): Boolean {
        return oldItem.ID == newItem.ID
    }

    override fun areContentsTheSame(oldItem: Valute, newItem: Valute): Boolean {
        return oldItem == newItem
    }

}