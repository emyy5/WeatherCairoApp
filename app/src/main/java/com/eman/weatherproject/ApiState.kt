package com.eman.weatherproject

import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.WeatherForecast


sealed class ApiState{
    data class onSuccess(val weatherForecast: WeatherForecast):ApiState()
    data class onFail(val msg: Throwable ):ApiState()
    data class Loading(val m:String) : ApiState()
    }

