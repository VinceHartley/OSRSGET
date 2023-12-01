package com.hartleyv.android.osrsget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FilterCriteria(
    val searchTerm: String?,
    val orderColumn: String?,
    val orderDirection: String?,
    val buyPriceMin: Int?,
    val buyPriceMax: Int?,
    val sellPriceMin: Int?,
    val sellPriceMax: Int?,
    val volumeMin: Int?,
    val volumeMax: Int?,
    val marginMin: Int?,
    val marginMax: Int?,
    val buyLimitMin: Int?,
    val buyLimitMax: Int?
)

class SharedViewModel  : ViewModel() {
    private val _filterCriteria = MutableLiveData<FilterCriteria>()
    private val _filteredItems = MutableStateFlow<List<CombinedItemListInfo>>(emptyList())
    val filteredItems: StateFlow<List<CombinedItemListInfo>> = _filteredItems

    private val itemRepository = ItemRepository.get()


    fun getFilterCriteria(): LiveData<FilterCriteria> = _filterCriteria



    init {

        CoroutineScope(Dispatchers.IO).launch {

            itemRepository.getCombinedItemInfo().collect {
                _filteredItems.value = it
            }

        }
    }

    fun updateFilterCriteria(newCriteria: FilterCriteria) {
        _filterCriteria.value = newCriteria
    }




}
