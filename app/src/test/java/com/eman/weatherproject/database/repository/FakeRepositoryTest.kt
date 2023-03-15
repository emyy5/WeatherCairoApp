package com.eman.weatherproject.database.repository

import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
//
//
 class FakeRepositoryTest {
}

//
//     private var favoriteList: MutableList<WeatherAddress> = mutableListOf<WeatherAddress>(),
//     private var alertList: MutableList<AlertData> = mutableListOf<AlertData>(),
//     private var weatherResponse: WeatherForecast = WeatherForecast(
//         46,
//         655,
//         584,
//         "asdjadsk",
//         565,
//         null,
//         emptyList(),
//         emptyList(),
//         emptyList()
//     )
//     ) : RepositoryInterface {
//
//         override suspend fun insertFavouritePlace(favouritePlace: FavouritePlace) {
//             favoriteList.add(favouritePlace)
//         }
//
//         override fun getAllFavouritePlaces(): Flow<List<FavouritePlace>> = flow {
//             emit(favoriteList)
//         }
//
//         override suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace) {
//             favoriteList.remove(favouritePlace)
//         }
//
//         override suspend fun deleteAlert(alert: LocalAlert) {
//             alertList.remove(alert)
//         }
//
//         override suspend fun insertAlert(alert: LocalAlert) {
//             alertList.add(alert)
//         }
//
//         override fun getAllAlerts(): Flow<List<LocalAlert>> = flow {
//             emit(alertList)
//         }
//
//         override fun insertCurrentWeather(root: Root) {
//             TODO("Not yet implemented")
//         }
//
//         override suspend fun deleteCurrentWeather() {
//             TODO("Not yet implemented")
//         }
//
//         override suspend fun getCurrentWeathers(latLng: LatLng): Flow<Root> = flow {
//             emit(weatherResponse)
//         }
//
//         override suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow{
//             emit(weatherResponse)
//         }
//     }
//
//
