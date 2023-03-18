package com.eman.weatherproject.database.network

import com.eman.weatherproject.database.model.WeatherForecast


private const val TAG = "RemoteSource"
class RemoteSource: RemoteSourceInterface {

    lateinit var MyCurrentWeather: WeatherForecast
   override suspend fun getCurrentWeatherWithLocation(
        lat: Double,
        long: Double,
        unit: String,
        lang: String
    ): WeatherForecast {

       val apiService= RetrofitHelper.getInstance().create(API_Interface::class.java)

        val apiResponse = apiService.getTheWholeWeather(lat, long, unit, "minutely", lang,
            "375d11598481406538e244d548560243")

        return apiResponse.body() as WeatherForecast
    }


    companion object {
        private var instance: RemoteSource? = null
        fun getInstance(): RemoteSource {
            return instance ?: RemoteSource()
        }
    }
}
