package com.hartleyv.android.osrsget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frosch2010.fuzzywuzzy_kotlin.FuzzySearch
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val TAG = "ItemListViewModel"

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

class ItemListViewModel: ViewModel () {
    private val itemRepository = ItemRepository.get()
    private val _listItems: MutableStateFlow<List<CombinedItemListInfo>> =
        MutableStateFlow(emptyList())

    var listItems: StateFlow<List<CombinedItemListInfo>> = _listItems.asStateFlow()
        get() = _listItems.asStateFlow()


    private val _filteredItems = MutableStateFlow<List<CombinedItemListInfo>>(emptyList())
    var filteredItems: StateFlow<List<CombinedItemListInfo>> = _filteredItems.asStateFlow()
    private val _filterCriteria = MutableLiveData<FilterCriteria>(
        FilterCriteria(null, null, null, null, null, null, null, null, null, null, null, null, null)
    )

    private var initalDataRetreived = false;
    var setupWorkerFinished = false;


    init {
        viewModelScope.launch {
            itemRepository.getCombinedItemInfo().collect {

                // update initally from the database
                if (!initalDataRetreived) {
                    initalDataRetreived = true
                    _listItems.value = it
                    _filteredItems.value = it
                } else if (setupWorkerFinished) {
                    // update after all wiki polls happen
                    Log.e("HERE", "VIEWMODEL RESPONDED TO WORKER FINISHED")
                    _listItems.value = it
                    _filteredItems.value = it
                    applySettings(_filterCriteria.value)
                }


            }
        }
    }

    fun applySettings(filterCriteria: FilterCriteria?) {
        val originalList = listItems.value
        val sortedList: List<CombinedItemListInfo>

        // sort the list ascending or descending based on selected criteria
        sortedList = when(filterCriteria?.orderColumn.toString().lowercase()) {
            "buy price" -> {
                if (filterCriteria?.orderDirection == "Descending") {
                    originalList.sortedBy { it.high }.reversed()
                } else {
                    originalList.sortedBy { it.high }
                }
            }

            "sell price" -> {
                if (filterCriteria?.orderDirection == "Descending") {
                    originalList.sortedBy { it.low }.reversed()
                } else {
                    originalList.sortedBy { it.low }
                }
            }

            "daily volume" -> {
                if (filterCriteria?.orderDirection == "Descending") {
                    originalList.sortedBy { it.totalVolume }.reversed()
                } else {
                    originalList.sortedBy { it.totalVolume }
                }
            }

            "margin" -> {
                if (filterCriteria?.orderDirection == "Descending") {
                    originalList.sortedBy { it.margin }.reversed()
                } else {
                    originalList.sortedBy { it.margin }
                }
            }

            else -> {
                originalList
            }
        }

        // apply a fuzzywuzzy comparison to string search names
        var searchedList = sortedList
        if (filterCriteria?.searchTerm != null && filterCriteria.searchTerm != "") {
            searchedList = sortedList.filter {
                FuzzySearch.partialRatio(
                    it.itemName?.lowercase() ?: "",
                    filterCriteria.searchTerm.lowercase()
                ) >= 80
            }
        }

        // filter the list based on criteria
        val filteredList = searchedList.filter { item ->
            (filterCriteria?.buyPriceMin == null || item.high != null && item.high >= filterCriteria.buyPriceMin) &&
                    (filterCriteria?.buyPriceMax == null || item.high != null && item.high <= filterCriteria.buyPriceMax) &&
                    (filterCriteria?.sellPriceMin == null || item.low != null && item.low >= filterCriteria.sellPriceMin) &&
                    (filterCriteria?.sellPriceMax == null || item.low != null && item.low <= filterCriteria.sellPriceMax) &&
                    (filterCriteria?.volumeMin == null || item.totalVolume != null && item.totalVolume >= filterCriteria.volumeMin) &&
                    (filterCriteria?.volumeMax == null || item.totalVolume != null && item.totalVolume <= filterCriteria.volumeMax) &&
                    (filterCriteria?.marginMin == null || item.margin != null && item.margin >= filterCriteria.marginMin) &&
                    (filterCriteria?.marginMax == null || item.margin != null && item.margin <= filterCriteria.marginMax) &&
                    (filterCriteria?.buyLimitMin == null || item.buyLimit != null && item.buyLimit >= filterCriteria.buyLimitMin) &&
                    (filterCriteria?.buyLimitMax == null || item.buyLimit != null && item.buyLimit <= filterCriteria.buyLimitMax)
        }

        this.updateFilteredList(filteredList)

    }


    suspend fun deleteInstantPrices() {
        itemRepository.deleteInstantPrices()
    }

    fun updateFilterCriteria(newCriteria: FilterCriteria) {
        _filterCriteria.value = newCriteria
    }
    fun getFilterCriteria(): LiveData<FilterCriteria> = _filterCriteria

    fun updateFilteredList(filtered: List<CombinedItemListInfo>) {
        _filteredItems.value = filtered
    }
}