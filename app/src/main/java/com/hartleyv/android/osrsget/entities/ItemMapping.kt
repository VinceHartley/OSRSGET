package com.hartleyv.android.osrsget.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_mapping")
data class ItemMapping(
    @PrimaryKey val itemId: Int,
    val members: Boolean?,
    val lowAlch: Int?,
    val limit: Int?,
    val highAlch: Int?,
    val name: String?,
    val examine: String?,
    val icon: String?
)