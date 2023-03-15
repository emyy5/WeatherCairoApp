package com.eman.weatherproject.database.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

        const val base_URL = "https://api.openweathermap.org/data/2.5/"
        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }




