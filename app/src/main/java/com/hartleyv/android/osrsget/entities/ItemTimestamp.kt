package com.hartleyv.android.osrsget.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_timestamp", primaryKeys = ["itemId", "timestep"])
data class ItemTimestamp(
    val itemId: Int,
    val timestep: String,
    val timestamp: Int?,
    val highPrice: Int?,
    val lowPrice: Int?,
    val highPriceVolume: Int?,
    val lowPriceVolume: Int?
)