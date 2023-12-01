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

    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "AND itemName LIKE (:name)")
    fun getSearchedCombinedItemInfo(name: String): Flow<List<CombinedItemListInfo>>



    // SORTING FUNCTIONS
    /*

    columns to sort by:

    buy price
    sell price
    daily volume
    margin


    */

    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY high ASC")
    fun getSortedHighPriceAscending(): Flow<List<CombinedItemListInfo>>

    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY high DESC")
    fun getSortedHighPriceDescending(): Flow<List<CombinedItemListInfo>>

    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY low ASC")
    fun getSortedLowPriceAscending(): Flow<List<CombinedItemListInfo>>

    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY low DESC")
    fun getSortedLowPriceDescending(): Flow<List<CombinedItemListInfo>>


    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY totalVolume ASC")
    fun getSortedVolumeAscending(): Flow<List<CombinedItemListInfo>>


    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY totalVolume DESC")
    fun getSortedVolumeDescending(): Flow<List<CombinedItemListInfo>>

    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY margin ASC")
    fun getSortedMarginAscending(): Flow<List<CombinedItemListInfo>>


    @Transaction
    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
            "instant_price.high as high, instant_price.low as low, " +
            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
            "item_mapping.`limit` as buyLimit, " +
            "instant_price.high - instant_price.low as margin " +
            "FROM item_mapping, item_timestamp, instant_price " +
            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
            "ORDER BY margin DESC")
    fun getSortedMarginDescending(): Flow<List<CombinedItemListInfo>>




    //todo filtering needs to be done wherever the above queries return
    // this is because we can have a filter for multiple parts, and also have a sort
    // order will be
    // 1. call default on statup + no sort
    // 2. call sort on sort
    // 3. filter returned data
    // 4. display returned data


    // i need to make another fragment to open for this filtering/sorting functionality

    /*
    sorting:
    button menu, only one option selected
    another button menu for descending/ascending

    filtering:
    some text boxes with min and max entry areas

     */










//    // FILTER FUNCTIONS
//    /*
//
//    filters from the wiki site:
//
//    buy limit
//    buy price
//    sell price
//    margin
//    daily volume
//    potential profit
//    margin * volume
//
//
//
//     */
//
//    /*
//    my filters
//
//    buy price
//    sell price
//    volume
//    margin
//    buy limit
//     */
//
//    @Transaction
//    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
//            "instant_price.high as high, instant_price.low as low, " +
//            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
//            "item_mapping.`limit` as buyLimit, " +
//            "instant_price.high - instant_price.low as margin " +
//            "FROM item_mapping, item_timestamp, instant_price " +
//            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
//            "AND high >=(:min) AND high <= (:max)")
//    fun getFilteredHighPrice(min: Int, max:Int): Flow<List<CombinedItemListInfo>>
//
//    @Transaction
//    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
//            "instant_price.high as high, instant_price.low as low, " +
//            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
//            "item_mapping.`limit` as buyLimit, " +
//            "instant_price.high - instant_price.low as margin " +
//            "FROM item_mapping, item_timestamp, instant_price " +
//            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
//            "AND low >=(:min) AND low <= (:max)")
//    fun getFilteredLowPrice(min: Int, max:Int): Flow<List<CombinedItemListInfo>>
//
//
//    @Transaction
//    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
//            "instant_price.high as high, instant_price.low as low, " +
//            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
//            "item_mapping.`limit` as buyLimit, " +
//            "instant_price.high - instant_price.low as margin " +
//            "FROM item_mapping, item_timestamp, instant_price " +
//            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
//            "AND totalVolume >=(:min) AND totalVolume <= (:max)")
//    fun getFilteredVolume(min: Int, max:Int): Flow<List<CombinedItemListInfo>>
//
//    @Transaction
//    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
//            "instant_price.high as high, instant_price.low as low, " +
//            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
//            "item_mapping.`limit` as buyLimit, " +
//            "instant_price.high - instant_price.low as margin " +
//            "FROM item_mapping, item_timestamp, instant_price " +
//            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
//            "AND margin >=(:min) AND margin <= (:max)")
//    fun getFilteredMargin(min: Int, max:Int): Flow<List<CombinedItemListInfo>>
//
//    @Transaction
//    @Query("SELECT item_mapping.itemId as itemId, item_mapping.name as itemName, " +
//            "instant_price.high as high, instant_price.low as low, " +
//            "item_timestamp.highPriceVolume + item_timestamp.lowPriceVolume as totalVolume, " +
//            "item_mapping.`limit` as buyLimit, " +
//            "instant_price.high - instant_price.low as margin " +
//            "FROM item_mapping, item_timestamp, instant_price " +
//            "WHERE item_mapping.itemId = item_timestamp.itemId AND item_mapping.itemId = instant_price.itemId " +
//            "AND buyLimit >=(:min) AND buyLimit <= (:max)")
//    fun getFilteredBuyLimit(min: Int, max:Int): Flow<List<CombinedItemListInfo>>


}