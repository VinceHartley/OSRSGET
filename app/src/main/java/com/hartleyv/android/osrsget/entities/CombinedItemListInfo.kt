package com.hartleyv.android.osrsget.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CombinedItemListInfo(
    val itemId: Int,
    val itemName: String?,
    val high: Int?,
    val low: Int?,
    val totalVolume: Int?,
    val buyLimit: Int?,
    val margin: Int?
) : Parcelable


