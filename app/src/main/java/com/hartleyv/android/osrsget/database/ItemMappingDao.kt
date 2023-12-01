package com.hartleyv.android.osrsget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hartleyv.android.osrsget.entities.ItemMapping
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemMappingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemMappings(itemMappings: List<ItemMapping>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemMapping(itemMapping: ItemMapping)

//    @Query("SELECT * FROM itemmapping")
//    fun getItemMappings(): Flow<List<ItemMapping>>

//    @Query("SELECT * FROM itemmapping WHERE itemId =(:id)")
//    fun getItemMapping(id: Int) : ItemMapping

}