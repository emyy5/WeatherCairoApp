package com.eman.weatherproject.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eman.weatherproject.database.model.AlertData
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDAO {
    @Query("SELECT * FROM alerts")
    fun storedAllAlert():Flow<List<AlertData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlert(alert: AlertData)

    @Delete
    fun deleteAlert(alert: AlertData)
}