package com.eman.weatherproject.database.room

import androidx.lifecycle.LiveData
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast

interface LocalSourceInterface {


    fun getMyAllAddress(): LiveData<List<WeatherAddress>>
    fun insertFavAddress(address: WeatherAddress)
    fun deleteFavAddress(address: WeatherAddress)


    fun getAllWeathersStored(): LiveData<List<WeatherForecast>>
    fun getWeatherWithLatLong(lat:Double,long:Double): LiveData<WeatherForecast>
    fun insertWeather(weather: WeatherForecast)
    fun deleteWeather(weather: WeatherForecast)


    fun getAllStoredAlerts():LiveData<List<AlertData>>
    fun insertAlert(alert: AlertData)
    fun deleteAlert(alert: AlertData)
}
