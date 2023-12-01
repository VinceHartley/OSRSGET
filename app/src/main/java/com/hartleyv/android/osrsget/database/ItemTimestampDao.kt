package com.hartleyv.android.osrsget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hartleyv.android.osrsget.entities.InstantPrice
import com.hartleyv.android.osrsget.entities.ItemTimestamp
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemTimestamp(itemTimestamp: ItemTimestamp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemTimestamps(itemTimestamp: List<ItemTimestamp>)
//
//    @Query("SELECT * FROM itemtimestamp")
//    fun getItems(): Flow<List<ItemTimestamp>>
}