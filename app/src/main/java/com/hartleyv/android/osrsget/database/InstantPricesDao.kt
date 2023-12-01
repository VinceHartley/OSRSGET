package com.hartleyv.android.osrsget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hartleyv.android.osrsget.entities.InstantPrice
import com.hartleyv.android.osrsget.entities.InstantPriceAndMapping
import kotlinx.coroutines.flow.Flow

@Dao
interface InstantPricesDao {
//    @Query("SELECT * FROM instantprice")
//    fun getItems(): Flow<List<InstantPrice>>
//
//    @Query("SELECT * FROM item WHERE id=(:id)")
//    suspend fun getItem(id: Int): InstantPrice

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInstantPrice(instantPrice: InstantPrice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInstantPrices(instantPrice: List<InstantPrice>)

    @Query("DELETE FROM instant_price")
    fun deleteInstantPrices()


    @Transaction
    @Query("Select * from instant_price")
    fun getInstantPricesAndMapping(): Flow<List<InstantPriceAndMapping>>

}