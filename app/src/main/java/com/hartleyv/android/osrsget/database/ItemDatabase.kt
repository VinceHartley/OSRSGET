package com.hartleyv.android.osrsget.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hartleyv.android.osrsget.entities.InstantPrice
import com.hartleyv.android.osrsget.entities.ItemMapping
import com.hartleyv.android.osrsget.entities.ItemTimestamp

@Database(entities = [InstantPrice::class, ItemMapping::class, ItemTimestamp::class], version = 6)
//@TypeConverters(ItemTypeConverters::class)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun instantPricesDao(): InstantPricesDao
    abstract fun itemMappingDao(): ItemMappingDao
    abstract fun itemTimestampDao(): ItemTimestampDao
    abstract fun combinedDao(): CombinedDao
}