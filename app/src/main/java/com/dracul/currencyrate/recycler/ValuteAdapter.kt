package com.example.technotestvk.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dracul.currencyrate.R
import com.dracul.currencyrate.data.Valute
import com.dracul.currencyrate.databinding.ItemValuteBinding
import com.dracul.currencyrate.recycler.ProductDiffCallback
import com.dracul.currencyrate.roundOffDecimal
import kotlin.math.roundToInt


class ValuteAdapter : ListAdapter<Valute, ValuteAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemValuteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemValuteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Valute


        fun bind(item: Valute) {
            this.item = item
            binding.run {
                value.text = item.Value.roundOffDecimal().toString()
                if (item.Value>item.Previous)
                    value.setTextColor(Color.GREEN)
                else if (item.Value<item.Previous)
                    value.setTextColor(Color.RED)
                else
                    value.setTextColor(ContextCompat.getColor(binding.root.context ,com.google.android.material.R.attr.colorOnPrimary))

                nominal.text = item.Nominal.toString()
                name.text = item.Name
                charCode.text = item.CharCode
            }
        }
    }
}

