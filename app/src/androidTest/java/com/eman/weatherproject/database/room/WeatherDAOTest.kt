package com.eman.weatherproject.database.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eman.weatherproject.database.model.CurrentWeather
import com.eman.weatherproject.database.model.WeatherForecast
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDb
    private lateinit var dao: WeatherDAO

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), WeatherDb::class.java
        ).allowMainThreadQueries().build()

        dao = database.weatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertWeather() {
        val weather = WeatherForecast(
            33.3, 35.55555, "",
            CurrentWeather(0, 0.0, 0, 0, 0, 0.0, listOf()), listOf(), listOf(), listOf()
        )
        dao.insertWeather(weather)

        val getAllWeather = dao.myAllWeather()

        //(getAllWeather.contains(weather))

    }

    @Test
    fun deleteWeather() {
        val weather = WeatherForecast(
            33.3, 35.55555, "",
            CurrentWeather(0, 0.0, 0, 0, 0, 0.0, listOf()), listOf(), listOf(), listOf()
        )
        dao.deleteWeather(weather)

        val getAllWeather = dao.myAllWeather()

       // assert(!getAllWeather.contains(weather))
    }
}



