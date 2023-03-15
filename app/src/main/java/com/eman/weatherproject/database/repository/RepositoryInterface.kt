package com.eman.weatherproject.database.repository

import androidx.lifecycle.LiveData
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.Settings
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast

interface RepositoryInterface {
    suspend fun getCurrentWetherInRepo(
        lat: Double,
        long: Double,
        unit: String
    ): WeatherForecast



    val storedAddresses: LiveData<List<WeatherAddress>>

    fun getAllWeathersInRepo(): LiveData<List<WeatherForecast>>
    fun getMyWeatherOne(lat: Double, long: Double): LiveData<WeatherForecast>

    fun insertFavoriteAddress(address: WeatherAddress)
    fun deleteFavoriteAddress(address: WeatherAddress)

    fun insertWeather(weather: WeatherForecast)
    fun deleteWeather(weather: WeatherForecast)

    fun addSettingToSP(settings: Settings)
    fun getSettingToSP(): Settings?

    fun addWeatherToSP(weather: WeatherForecast)
    fun getWeatherToSP(): WeatherForecast?

   fun getAllAlert():LiveData<List<AlertData>>
   fun insertMyAlert(alert: AlertData)
   fun deleteMyAlert(alert: AlertData)
}
