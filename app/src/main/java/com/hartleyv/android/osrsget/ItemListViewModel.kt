package com.hartleyv.android.osrsget

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



private const val TAG = "ItemListViewModel"

class ItemListViewModel: ViewModel () {
    private val itemRepository = ItemRepository.get()
    private val _listItems: MutableStateFlow<List<CombinedItemListInfo>> =
        MutableStateFlow(emptyList())

    var listItems: StateFlow<List<CombinedItemListInfo>> = _listItems.asStateFlow()
        get() = _listItems.asStateFlow()


    init {
        CoroutineScope(Dispatchers.IO).launch {

            itemRepository.getCombinedItemInfo().collect {
                _listItems.value = it
            }

        }
    }


    suspend fun deleteInstantPrices() {
        itemRepository.deleteInstantPrices()
    }


}