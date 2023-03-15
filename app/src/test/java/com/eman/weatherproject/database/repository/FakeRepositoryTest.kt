//package com.eman.weatherproject.database.repository
//
//import android.provider.DocumentsContract
//import com.eman.weatherproject.database.model.AlertData
//import com.eman.weatherproject.database.model.WeatherAddress
//import com.google.android.gms.maps.model.LatLng
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//
//class FakeRepository(
// private var favoriteList: MutableList<WeatherAddress> = mutableListOf<WeatherAddress>(),
// private var alertList: MutableList<AlertData> = mutableListOf<AlertData>(),
// private var weatherResponse: DocumentsContract.Root = Root(
//  46,
//  655,
//  584,
//  "Emyyyyyyyyyy",
//  565,
//  null,
//  emptyList(),
//  emptyList(),
//  emptyList()
// )
//) : RepositoryInterface {
//
// override suspend fun insertFavouritePlace(favouritePlace: WeatherAddress) {
//  favoriteList.add(favouritePlace)
// }
//
// override fun getAllFavouritePlaces(): Flow<List<WeatherAddress>> = flow {
//  emit(favoriteList)
// }
//
// override suspend fun deleteFavouritePlace(favouritePlace: WeatherAddress) {
//  favoriteList.remove(favouritePlace)
// }
//
// override suspend fun deleteAlert(alert: AlertData) {
//  alertList.remove(alert)
// }
//
// override suspend fun insertAlert(alert: AlertData) {
//  alertList.add(alert)
// }
//
// override fun getAllAlerts(): Flow<List<AlertData>> = flow {
//  emit(alertList)
// }
//
// override fun insertCurrentWeather(root: DocumentsContract.Root) {
//  TODO("Not yet implemented")
// }
//
// override suspend fun deleteCurrentWeather() {
//  TODO("Not yet implemented")
// }
//
// override suspend fun getCurrentWeathers(latLng: LatLng): Flow<DocumentsContract.Root> = flow {
//  emit(weatherResponse)
// }
//
// override suspend fun getFavouriteWeather(favouritePlace: WeatherAddress): Flow<DocumentsContract.Root> = flow{
//  emit(weatherResponse)
// }
//}