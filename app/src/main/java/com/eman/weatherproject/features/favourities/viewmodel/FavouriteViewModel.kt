package com.eman.weatherproject.features.favourities.viewmodel

import androidx.lifecycle.*
import com.eman.weatherproject.database.model.Settings
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavouriteViewModel(private val repo: RepositoryInterface): ViewModel() {

    private val _favWeatherFromNetwork = MutableLiveData<WeatherForecast>()
    val favWeatherFromNetwork: LiveData<WeatherForecast> = _favWeatherFromNetwork

    fun getFavWholeWeather(lat:Double,long:Double,unit:String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getCurrentWetherInRepo(lat,long,unit)
            _favWeatherFromNetwork.postValue(response)
        }
    }


    //Database
    fun getAllAddresses(): Flow<List<WeatherAddress>> {
        return repo.storedAddresses
    }

    fun getAllWeathersInVM(): Flow<List<WeatherForecast>> {
        return repo.getAllWeathersInRepo()
    }

    fun addAddressToFavorites(address: WeatherAddress){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertFavoriteAddress(address)
        }
        //updateWeatherDatabase(owner)
    }

    fun removeAddressFromFavorites(address: WeatherAddress){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteFavoriteAddress(address)
        }
    }



    fun getOneWeather(lat:Double,long:Double): Flow<WeatherForecast> {
        return repo.getMyWeatherOne(lat,long)
    }

    fun addOneFavWeather(weather: WeatherForecast){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertWeather(weather)
        }
    }

    fun removeOneFavWeather(weather: WeatherForecast){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteWeather(weather)
        }
    }


    fun updateWeatherDatabase(owner: LifecycleOwner){
        viewModelScope.launch {
            getAllAddresses().collect{
                for (favWeather in it){
                    getFavWholeWeather(favWeather.lat,favWeather.lon,"metric")
                    favWeatherFromNetwork.observe(owner) {item ->
                        addOneFavWeather(item)
                    }
                }
            }
        }

    }

    fun setSettingsSharedPrefs(settings: Settings){
        repo.addSettingToSP(settings)
    }

    fun getStoredSettings(): Settings? {
        return repo.getSettingToSP()
    }


}
