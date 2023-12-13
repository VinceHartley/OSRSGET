package com.hartleyv.android.osrsget

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hartleyv.android.osrsget.databinding.ListItemItemBinding
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import java.text.NumberFormat

class ItemHolder(
    private val binding: ListItemItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CombinedItemListInfo) {

        binding.itemName.text = item.itemName
        binding.highPrice.text = NumberFormat.getInstance().format(item.high)
        binding.lowPrice.text =  NumberFormat.getInstance().format(item.low)
        binding.dailyVolume.text = NumberFormat.getInstance().format(item.totalVolume)
        binding.margin.text = NumberFormat.getInstance().format(item.margin)

        val imageId = itemView.context.resources.getIdentifier("image_${item.itemId}", "drawable", itemView.context.packageName)
        binding.itemImage.setImageResource(imageId)

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