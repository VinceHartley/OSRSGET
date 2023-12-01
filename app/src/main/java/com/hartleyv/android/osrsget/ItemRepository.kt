package com.hartleyv.android.osrsget

import android.content.Context
import androidx.room.Room
import com.hartleyv.android.osrsget.database.ItemDatabase
import com.hartleyv.android.osrsget.entities.InstantPrice
import com.hartleyv.android.osrsget.entities.ItemMapping
import com.hartleyv.android.osrsget.entities.ItemTimestamp

private const val DATABASE_NAME = "item-database"

class ItemRepository private constructor(context: Context) {

    private val database: ItemDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ItemDatabase::class.java,
            DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()

//    fun getItems(): Flow<List<InstantPrice>> = database.instantPricesDao().getItems()
//    suspend fun getItem(id: Int): InstantPrice = database.itemDao().getItem(id)

    suspend fun deleteInstantPrices() = database.instantPricesDao().deleteInstantPrices()

//    suspend fun getItemMapping(id: Int) = database.itemMappingDao().getItemMapping(id)

    suspend fun addItemMappings(itemMappings: List<ItemMapping>) =
        database.itemMappingDao().addItemMappings(itemMappings)

    suspend fun addItemMapping(itemMapping: ItemMapping) =
        database.itemMappingDao().addItemMapping(itemMapping)

    suspend fun addInstantPrice(instantPrice: InstantPrice) {
        database.instantPricesDao().addInstantPrice(instantPrice)
    }

    suspend fun addInstantPrices(instantPrice: List<InstantPrice>) {
        database.instantPricesDao().addInstantPrices(instantPrice)
    }

    suspend fun getInstantPricesAndMapping() = database.instantPricesDao().getInstantPricesAndMapping()

    suspend fun addItemTimestamp(itemTimestamp: ItemTimestamp) =
        database.itemTimestampDao().addItemTimestamp(itemTimestamp)

    suspend fun addItemTimestamps(itemTimestamps: List<ItemTimestamp>) =
        database.itemTimestampDao().addItemTimestamps(itemTimestamps)




    // Combined repo functions
    suspend fun getCombinedItemInfo() =
        database.combinedDao().getCombinedItemInfo()

    suspend fun getSearchedCombinedItemInfo(name: String) =
        database.combinedDao().getSearchedCombinedItemInfo(name)

    // sorting
    suspend fun getSortedHighPriceAscending() =
        database.combinedDao().getSortedHighPriceAscending()

    suspend fun getSortedHighPriceDescending() =
        database.combinedDao().getSortedHighPriceDescending()

    suspend fun getSortedLowPriceAscending() =
        database.combinedDao().getSortedLowPriceAscending()

    suspend fun getSortedLowPriceDescending() =
        database.combinedDao().getSortedLowPriceDescending()

    suspend fun getSortedVolumeAscending() =
            database.combinedDao().getSortedVolumeAscending()

    suspend fun getSortedVolumeDescending() =
        database.combinedDao().getSortedVolumeDescending()

    suspend fun getSortedMarginAscending() =
        database.combinedDao().getSortedMarginAscending()

    suspend fun getSortedMarginDescending() =
        database.combinedDao().getSortedMarginDescending()






    companion object {
        private var INSTANCE: ItemRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ItemRepository(context)
            }
        }

        fun get(): ItemRepository {
            return INSTANCE ?:
            throw IllegalStateException("InstantPrice repository not initialized")
        }
    }
}