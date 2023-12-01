package com.hartleyv.android.osrsget.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instant_price")
data class InstantPrice(
    @PrimaryKey val itemId: Int,
    val high: Int?,
    val highTime: Int?,
    val low: Int?,
    val lowTime: Int?,
)