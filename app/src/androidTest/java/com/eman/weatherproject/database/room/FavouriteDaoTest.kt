package com.eman.weatherproject.database.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.eman.weatherproject.database.model.WeatherAddress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest


class FavouriteDaoTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()
    private lateinit var database: WeatherDb
    lateinit var dao: FavouriteDao
    @Before
    fun setUp() {
    database = Room.inMemoryDatabaseBuilder( ApplicationProvider.getApplicationContext(),
        WeatherDb::class.java)
        .allowMainThreadQueries().build()
dao=database.addressesDao()
    }
       @After
    fun tearDown()
       {
           database.close()
        }
     @Test
    fun insertFavAddress() =
        runBlockingTest {

              val data1 = WeatherAddress( "Ismailia", 32.00,  32.00)
            //when
             dao.insertFavAddress(data1)

            //Then
             val result = dao.getAllFav()
           assertThat(result, IsNull.notNullValue())
           //assertThat(result).contains(data1)
            //assert(result.value(data1))


        }
    @Test
    fun myAllAddress_item() {
        val data1= WeatherAddress( "Ismailia", 32.00,  32.00)
        val data2= WeatherAddress("Ismailia",  32.00,  32.00)
        val data3= WeatherAddress( "Ismailia",  32.00, 32.00)
    dao.insertFavAddress(data1)
    dao.insertFavAddress(data2)
    dao.insertFavAddress(data3)
    //when
val result= dao.getAllFav()
    //Then
    assertThat(result.value, Is.`is`(3))

}
        @Test
    fun deleteFavAddress() = runBlockingTest {
        val data1 = WeatherAddress("hghmm",32.0,32.0)
        dao.insertFavAddress(data1)
        val outComeData=dao.getAllFav()

        //when
        dao.deleteFavAddress(data1)
        //Then
        val result= dao.getAllFav()
        assertThat(result.value, IsEmptyCollection.empty())
        assertThat(result.value, `is`(3))

         dao.insertFavAddress(data1)

            //Then
           // val result = dao.getAllFav()


    }


    }
