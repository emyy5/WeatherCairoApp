package com.eman.weatherproject.features.home.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.eman.weatherproject.ApiState
import com.eman.weatherproject.database.model.Settings
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel (val repo: Repository): ViewModel() {

    var apiState = MutableStateFlow<ApiState>(ApiState.Loading(""))
    private lateinit var currentWeather: WeatherForecast


    private val _weatherFromNetwork :MutableStateFlow<ApiState?> = MutableStateFlow(null)
    val weatherFromNetwork: StateFlow<ApiState?> =_weatherFromNetwork

    fun getWholeWeather(lat: Double, long: Double, unit: String) {
        Log.d(TAG, "Ifoooooooooooo: ")
        viewModelScope.launch(Dispatchers.IO) {
            _weatherFromNetwork.emit(ApiState.Loading(""))

            val response = repo.getCurrentWetherInRepo(lat, long, unit)
            _weatherFromNetwork.emit(ApiState.onSuccess(response))

        }

    }


    fun getStoredSettings(): Settings? {
        return repo.getSettingToSP()
    }

    fun getStoredCurrentWeather(): WeatherForecast? {
        return repo.getWeatherToSP()
    }


    fun addWeatherInVM(weather: WeatherForecast) {
        repo.addWeatherToSP(weather)
    }


    fun updateWeatherPrefs(owner: LifecycleOwner) {
        getWholeWeather(
            repo.getWeatherToSP()?.lat as Double,
            repo.getWeatherToSP()?.lon as Double,
            "metric"
        )
       viewModelScope.launch {
            weatherFromNetwork.collect {
                when (it) {
                    is ApiState.onSuccess -> {
                        repo.addWeatherToSP(it.weatherForecast)

                    }
                    is ApiState.Loading -> {

                    }
                    is ApiState.onFail -> {

                    }

                    else -> {}
                }
            }

        }





    }
}