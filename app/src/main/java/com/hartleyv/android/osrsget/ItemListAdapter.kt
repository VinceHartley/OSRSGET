package com.hartleyv.android.osrsget

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hartleyv.android.osrsget.databinding.ListItemItemBinding
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import com.hartleyv.android.osrsget.entities.InstantPrice
import com.hartleyv.android.osrsget.entities.InstantPriceAndMapping
import java.text.NumberFormat

class ItemHolder(
    private val binding: ListItemItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CombinedItemListInfo) {
        binding.itemId.text =
            "Item Id: " + item.itemId.toString()
        binding.itemName.text = "Item name: " + item.itemName
        binding.highPrice.text = "Item High Price: " + NumberFormat.getInstance().format(item.high)
        binding.lowPrice.text = "Item Low Price: " + NumberFormat.getInstance().format(item.low)
        binding.volume.text = "Daily Item Volume: " +
                NumberFormat.getInstance().format(item.totalVolume)
        binding.margin.text = "Margin: " + NumberFormat.getInstance().format(item.margin)

        if (item.buyLimit == null) {
            binding.buyLimit.text = "Buy Limit: None"
        } else {
            binding.buyLimit.text = "Buy Limit: " + NumberFormat.getInstance().format(item.buyLimit)
        }



        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${item.itemId} clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


class ItemListAdapter(
    private val itemListInfo: List<CombinedItemListInfo>
) : RecyclerView.Adapter<ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemItemBinding.inflate(inflater, parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemListInfo.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = itemListInfo[position]

        holder.bind(item)

    }


}