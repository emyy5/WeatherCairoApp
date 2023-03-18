package com.eman.weatherproject

import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class FakeRemoteSource(
    var weatherForecast: WeatherForecast
):RemoteSourceInterface {
    override suspend fun getCurrentWeatherWithLocation(
        lat: Double,
        long: Double,
        unit: String,
        lang: String,
    ): WeatherForecast {
        return weatherForecast
    }

    @Test
    fun deleteWeather() : Flow<List<WeathForecast>> = flow{
        emit(weatherList)
    }

    @Test
    fun addSettingToSP() : Flow<List<WeathForecast>> = flow{
        emit(weatherList)
    }

    @Test
    fun getSettingToSP() : Flow<List<WeathForecast>> = flow{
        emit(weatherList)
    }


}