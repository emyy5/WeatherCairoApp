package com.eman.weatherproject.database.repository

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eman.weatherproject.FakeLocalSource
import com.eman.weatherproject.FakeRemoteSource
import com.eman.weatherproject.database.model.CurrentWeather
import com.eman.weatherproject.database.model.Weather
import com.eman.weatherproject.database.model.WeatherForecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private lateinit var fakeLocalSource: FakeLocalSource
    private lateinit var fakeRemoteSource: FakeRemoteSource

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        fakeLocalSource = FakeLocalSource()
        fakeRemoteSource = FakeRemoteSource(
            weatherForecast = WeatherForecast(lat = 02.28, lon = 25.5, timezone = "454", daily = emptyList(), hourly = emptyList(), current = CurrentWeather(
                dt=56,
                temp=68.5,
                pressure=54,
                humidity=455,
                clouds=56,
                wind_speed = 56.456,
                weather = emptyList()
            ), alerts = emptyList()
            )
        )
    }

    @Test
    fun getCurrentWetherInRepo() =mainCoroutineRule.runBlockingTest {
       val result =  repository.getCurrentWetherInRepo(565.5,565.65,"dfs")
        assertThat(result, IsNull.notNullValue())
    }

    @Test
    fun getStoredAddresses()  =mainCoroutineRule.runBlockingTest {
        val result =  repository.storedAddresses.first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun getAllWeathersInRepo() =mainCoroutineRule.runBlockingTest {
        val result =  repository.getAllWeathersInRepo().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun getMyWeatherOne() =mainCoroutineRule.runBlockingTest {
        val result =  repository.getMyWeatherOne(32.4,32,4).first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun insertFavoriteAddress() =mainCoroutineRule.runBlockingTest {
        val result =  repository.insertFavoriteAddress().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun deleteFavoriteAddress() =mainCoroutineRule.runBlockingTest {
        val result =  repository.deleteFavoriteAddress().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun insertWeather() =mainCoroutineRule.runBlockingTest {
        val result =  repository.insertWeather().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun deleteWeather() =mainCoroutineRule.runBlockingTest {
        val result =  repository.deleteWeather().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun addSettingToSP() =mainCoroutineRule.runBlockingTest {
        val result =  repository.addSettingToSP().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun getSettingToSP() =mainCoroutineRule.runBlockingTest {
        val result =  repository.getSettingToSP().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun addWeatherToSP() =mainCoroutineRule.runBlockingTest {
        val result =  repository.addWeatherToSP().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

    @Test
    fun getWeatherToSP() =mainCoroutineRule.runBlockingTest {
        val result =  repository.getWeatherToSP().first()
        assertThat(result.size as Int, Is.`is`(fakeLocalSource.weatherList.size))
    }

}