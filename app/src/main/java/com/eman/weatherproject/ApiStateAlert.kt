package com.eman.weatherproject

import com.eman.weatherproject.database.model.AlertData


sealed class ApiStateAlert{
    data  class onSuccess(var listAlert: List<AlertData>):ApiStateAlert()
    data class onFail(val msg: String ):ApiStateAlert()
    data class Loading(val m:String) : ApiStateAlert()
    }

