package com.eman.weatherproject.features.favourities.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.model.CurrentWeather
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.repository.RepositoryInterface
import com.eman.weatherproject.database.room.FavouriteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//@Config(manifest= Config.NONE)
 class FavouriteViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
   // var mainCoroutineRule = MainCoroutineRule()

   // @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Fake Data
    private var favoriteList: MutableList<WeatherAddress> = mutableListOf<WeatherAddress>(
        WeatherAddress("eee", 45.5, 32.3),
        WeatherAddress("eee", 45.5, 24.3),
        WeatherAddress("rrr", 45.5, 32.4),
        WeatherAddress("ttt", 45.5, 32.3),
    )
    private var alertList: MutableList<AlertData> = mutableListOf<AlertData>(
        AlertData("eman","ee","22","22","ww",11,32,22,"fsdfd"),
        AlertData("eman","ee","22","22","ww",11,32,22,"fsdfd"),
        AlertData("eman","ee","22","22","ww",11,32,22,"fsdfd"),
        AlertData("eman","ee","22","22","ww",11,32,22,"fsdfd"),
        AlertData("eman","ee","22","22","ww",11,32,22,"fsdfd"),

        )

    private var weatherResponse: WeatherForecast = WeatherForecast(32.3, 32.4, "33", CurrentWeather(1,23.4,1,2,2,3.4,
        emptyList()
    ), emptyList(), emptyList(), emptyList())

    private lateinit var repository: RepositoryInterface
    private lateinit var favouriteViewModel: FavouriteViewModel

    @Before
    fun initFavouriteViewModelTest(){
      //  repository = FakeRepository(favoriteList,alertList,weatherResponse)
        favouriteViewModel = FavouriteViewModel(repository)
    }

//    @Test
//    fun getCurrentWeather() = runBlockingTest{
//        //Given
//        favouriteViewModel.getAllAddresses()
//        var data : FavouriteDao = WeatherForecast()
//        //When
//        val result = homeViewModel.root.first()
//        when(result){
//            is ApiState.Success -> {
//                data = result.data
//            }
//            is ApiState.Failure -> {
//            }
//            else -> {
//            }
//        }
        //Then
       // MatcherAssert.assertThat(data , IsNull.notNullValue())

}