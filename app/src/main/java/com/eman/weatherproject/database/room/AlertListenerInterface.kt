package com.eman.weatherproject.database.room

import com.eman.weatherproject.database.model.AlertData


interface AlertListenerInterface {

    fun removeAlertClick(alert: AlertData)
}

