package com.eman.weatherproject.database.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.Settings
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.network.RemoteSourceInterface
import com.eman.weatherproject.database.room.LocalSourceInterface
import com.eman.weatherproject.utilities.CURRENT_WEATHER
import com.eman.weatherproject.utilities.MyLanguage
import com.eman.weatherproject.utilities.SAVING_SETTINGS_IN_SHARED_PREFERENCES
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow


class Repository(var remoteSource: RemoteSourceInterface, var localSource: LocalSourceInterface,
                 var context: Context, var mySharedPre: SharedPreferences
) : RepositoryInterface {

    private lateinit var settings: Settings

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSourceInterface,
            localSource: LocalSourceInterface,
            context: Context,
            addSharedpre: SharedPreferences
        ): Repository {
            return instance ?: Repository(remoteSource, localSource, context, addSharedpre)
        }
    }

    override suspend fun getCurrentWetherInRepo(lat: Double, long: Double, unit: String, ): WeatherForecast {
        settings = Settings()
        if(getSettingToSP()?.language as Boolean) {
            val weatherinrepo =
                remoteSource.getCurrentWeatherWithLocation(lat, long, unit, MyLanguage.en.convertLanguage)
            return weatherinrepo
        }
            val weatherinrepo = remoteSource.getCurrentWeatherWithLocation(lat, long, unit, MyLanguage.ar.convertLanguage)
            return weatherinrepo

    }


    override val storedAddresses: Flow<List<WeatherAddress>>
        get() = localSource.getMyAllAddress()


    override fun getAllWeathersInRepo(): Flow<List<WeatherForecast>> {
        return localSource.getAllWeathersStored()
    }

    override fun getMyWeatherOne(lat: Double, long: Double): Flow<WeatherForecast> {
        return localSource.getWeatherWithLatLong(lat,long)
    }

    override fun insertFavoriteAddress(address: WeatherAddress) {
        localSource.insertFavAddress(address)
    }

    override fun deleteFavoriteAddress(address: WeatherAddress) {
        localSource.deleteFavAddress(address)
    }

    override fun insertWeather(weather: WeatherForecast) {
        localSource.insertWeather(weather)
    }

    override fun deleteWeather(weather: WeatherForecast) {
        localSource.deleteWeather(weather)
    }


    override fun addSettingToSP(settings: Settings) {
        val prefEditor = mySharedPre.edit()
        val gson = Gson()
        val settingStr = gson.toJson(settings)
        prefEditor.putString(SAVING_SETTINGS_IN_SHARED_PREFERENCES, settingStr)
        prefEditor.commit()
    }

    override fun getSettingToSP(): Settings? {
        val settingStr = mySharedPre.getString(SAVING_SETTINGS_IN_SHARED_PREFERENCES, "")
        val gson = Gson()
        val settingsObj = gson.fromJson(settingStr, Settings::class.java)
        return settingsObj
    }

    override fun addWeatherToSP(weather: WeatherForecast) {
        val prefEditor = mySharedPre.edit()
        val gson = Gson()
        val weatherStr = gson.toJson(weather)
        prefEditor.putString(CURRENT_WEATHER, weatherStr)
        prefEditor.commit()
    }

    override fun getWeatherToSP(): WeatherForecast? {
        val weatherStr = mySharedPre.getString(CURRENT_WEATHER, "")
        val gson = Gson()
        val weatherObj: WeatherForecast? = gson.fromJson(weatherStr, WeatherForecast::class.java)
        return weatherObj
    }


    override fun getAllAlert():Flow<List<AlertData>> {
      return  localSource.getAllStoredAlerts()
    }

    override fun insertMyAlert(alert: AlertData) {
        localSource.insertAlert(alert)
    }

    override fun deleteMyAlert(alert: AlertData) {
        localSource.deleteAlert(alert)
    }

}

