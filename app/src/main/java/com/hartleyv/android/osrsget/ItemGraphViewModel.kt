package com.hartleyv.android.osrsget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hartleyv.android.osrsget.entities.ItemTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemGraphViewModel: ViewModel()  {
    val wikiRepository = WikiRepository()
    private var itemId: Int = -1

    private val _itemPrices: MutableStateFlow<List<ItemTimestamp>> =
        MutableStateFlow(emptyList())
    var itemPrices: StateFlow<List<ItemTimestamp>> = _itemPrices.asStateFlow()
        get() = _itemPrices.asStateFlow()

    fun setItemId(itemId: Int) {
        this.itemId = itemId
    }

    fun getItemTimeseries() {

        viewModelScope.launch {
            //ping wiki for item timeseries

            val response = wikiRepository.fetchTimeseries("5m", itemId.toString())

            //save timeseries data in view model
            _itemPrices.value = response
        }
    }
}