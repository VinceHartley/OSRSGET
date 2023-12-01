package com.hartleyv.android.osrsget.entities

import androidx.room.Embedded
import androidx.room.Relation

data class InstantPriceAndMapping(
    @Embedded val instantPrice: InstantPrice,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "itemId"
    )
    val mapping: ItemMapping?
)