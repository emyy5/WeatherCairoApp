package com.eman.weatherproject.database.network

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class API_InterfaceTest {
    lateinit var apiCall: API_Interface

    @Before
    fun setUp() {
        apiCall = RetrofitHelper.getInstance().create(API_Interface::class.java)
    }

    @Test
    fun getTheWholeWeather() = runBlocking {
        //given
        val latitude = 32.0
        val longitude = 33.0
        //when
        val response = apiCall.getTheWholeWeather(
            lat = latitude,
            long = longitude,
            unit = "",
            exclude = "",
            lang = "",
            appid = "375d11598481406538e244d548560243"
        )
        assertThat(response.code(), `is`(200))

    }
}