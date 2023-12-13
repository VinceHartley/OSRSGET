package com.hartleyv.android.osrsget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hartleyv.android.osrsget.databinding.ListItemItemBinding
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import java.text.NumberFormat

class ItemHolder(
    private val binding: ListItemItemBinding,
    private val onItemClick: (CombinedItemListInfo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CombinedItemListInfo) {

        //set the data for list items
        binding.itemName.text = item.itemName
        binding.highPrice.text = NumberFormat.getInstance().format(item.high)
        binding.lowPrice.text =  NumberFormat.getInstance().format(item.low)
        binding.dailyVolume.text = NumberFormat.getInstance().format(item.totalVolume)
        binding.margin.text = NumberFormat.getInstance().format(item.margin)

        val imageId = itemView.context.resources.getIdentifier("image_${item.itemId}", "drawable", itemView.context.packageName)
        binding.itemImage.setImageResource(imageId)


        // list item click listener
        binding.root.setOnClickListener {
//            onItemClick.invoke(item)
        }
    }
}


class ItemListAdapter(
    private val itemListInfo: List<CombinedItemListInfo>,
    private val onItemClick: (CombinedItemListInfo) -> Unit
) : RecyclerView.Adapter<ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemItemBinding.inflate(inflater, parent, false)
        return ItemHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int {
        return itemListInfo.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = itemListInfo[position]

        holder.bind(item)

    }


}