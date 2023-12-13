package com.hartleyv.android.osrsget.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CombinedDao {
    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId")
    fun getCombinedItemInfo(): Flow<List<CombinedItemListInfo>>

}