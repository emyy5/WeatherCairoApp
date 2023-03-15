package com.eman.weatherproject.database.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import kotlinx.coroutines.flow.Flow


class LocalSource(context: Context): LocalSourceInterface {

    private var addressDao: FavouriteDao
    private var weatherDao: WeatherDAO
    private var alertsDao: AlertsDAO

    init {
        val db = WeatherDb.getInstance(context.applicationContext)
        addressDao = db.addressesDao()
        weatherDao = db.weatherDao()
        alertsDao = db.alertsDao()
    }

    companion object{
        private var localSource: LocalSource? = null
        fun getInstance(context: Context): LocalSource {
            if(localSource == null){
                localSource = LocalSource(context)
            }
            return localSource!!
        }
    }

    override fun getMyAllAddress(): Flow<List<WeatherAddress>> {
        return addressDao.getAllFav()
    }

    override fun insertFavAddress(address: WeatherAddress) {
        addressDao.insertFavAddress(address)
    }

    override fun deleteFavAddress(address: WeatherAddress) {
        addressDao.deleteFavAddress(address)
    }

    override fun getAllWeathersStored(): Flow<List<WeatherForecast>> {
        return weatherDao.myAllWeather()
    }

    override fun getWeatherWithLatLong(lat: Double, long: Double): Flow<WeatherForecast> {
        return weatherDao.weatherWithLatAndLong(lat,long)
    }

    override fun insertWeather(weather: WeatherForecast) {
        weatherDao.insertWeather(weather)
    }

    override fun deleteWeather(weather: WeatherForecast) {
        weatherDao.deleteWeather(weather)
    }

    override fun getAllStoredAlerts(): Flow<List<AlertData>> {
        val test  = alertsDao.storedAllAlert()
        return test

    }


    override fun insertAlert(alert: AlertData) {
        alertsDao.insertAlert(alert)
    }

    override fun deleteAlert(alert: AlertData) {
        alertsDao.deleteAlert(alert)
    }
}