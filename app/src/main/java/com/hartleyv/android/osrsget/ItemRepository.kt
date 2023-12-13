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


    suspend fun deleteInstantPrices() = database.instantPricesDao().deleteInstantPrices()

    suspend fun addItemMappings(itemMappings: List<ItemMapping>) =
        database.itemMappingDao().addItemMappings(itemMappings)

    suspend fun addInstantPrices(instantPrice: List<InstantPrice>) {
        database.instantPricesDao().addInstantPrices(instantPrice)
    }


    suspend fun addItemTimestamps(itemTimestamps: List<ItemTimestamp>) =
        database.itemTimestampDao().addItemTimestamps(itemTimestamps)




    // Combined repo functions
    suspend fun getCombinedItemInfo() =
        database.combinedDao().getCombinedItemInfo()





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