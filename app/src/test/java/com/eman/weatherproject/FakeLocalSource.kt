package com.eman.weatherproject

import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.room.LocalSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalSource(
    val weatherList:MutableList<WeatherAddress> = mutableListOf(),
    val weahterForecast: MutableList<WeatherForecast> = mutableListOf(),
    val alertData: MutableList<AlertData> = mutableListOf(),

//
):LocalSourceInterface {
    override fun getMyAllAddress(): Flow<List<WeatherAddress>> = flow{
        emit(weatherList)
    }

    override fun insertFavAddress(address: WeatherAddress) {
        weatherList.add(address)
    }

    override fun deleteFavAddress(address: WeatherAddress) {
        weatherList.remove(address)
    }

    override fun getAllWeathersStored(): Flow<List<WeatherForecast>> = flow{
        emit(weahterForecast)
    }

    override fun getWeatherWithLatLong(lat: Double, long: Double): Flow<WeatherForecast> = flow{
        emit(weahterForecast[0])
    }

    override fun insertWeather(weather: WeatherForecast) {
      weahterForecast.add(weather)
    }

    override fun deleteWeather(weather: WeatherForecast) {
        weahterForecast.remove(weather)
    }

    override fun getAllStoredAlerts(): Flow<List<AlertData>>  = flow{
        emit(alertData)
    }

    override fun insertAlert(alert: AlertData) {
        alertData.add(alert)
    }

    override fun deleteAlert(alert: AlertData) {
        alertData.remove(alert)
    }
}