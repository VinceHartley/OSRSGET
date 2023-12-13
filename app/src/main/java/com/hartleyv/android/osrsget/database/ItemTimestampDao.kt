package com.hartleyv.android.osrsget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.hartleyv.android.osrsget.entities.ItemTimestamp

@Dao
interface ItemTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemTimestamp(itemTimestamp: ItemTimestamp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemTimestamps(itemTimestamp: List<ItemTimestamp>)
}