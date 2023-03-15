package com.eman.weatherproject.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.eman.weatherproject.database.model.WeatherAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM addresses")
    fun getAllFav(): Flow<List<WeatherAddress>>

    @Insert
    fun insertFavAddress(address: WeatherAddress)

    @Delete
    fun deleteFavAddress(address: WeatherAddress)
}

