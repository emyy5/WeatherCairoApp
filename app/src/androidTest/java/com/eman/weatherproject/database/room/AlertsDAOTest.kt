package com.eman.weatherproject.database.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertsDAOTest {

    lateinit var database: WeatherDb
    lateinit var dao: AlertsDAO

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), WeatherDb::class.java
        ).allowMainThreadQueries().build()
        dao = database.alertsDao()

    }

    @After
    fun closeDB() = database.close()

    fun insertAlert() = runBlockingTest {
        val alert = AlertData(
            "address", "122", "133", "15/10", "20/10", 1, 2,
            111111111111,
            ""
        )
        dao.insertAlert(alert)

        val allShoppingItems = dao.storedAllAlert().getOrAwaitValue()

      //  MatcherAssert.assertThat(allShoppingItems).contains(alert)


    }

    fun deleteAlert() = runBlockingTest {
        val alert = AlertData(
            "address", "122", "133", "15/10", "20/10", 1, 2,
            111111111111,
            ""
        )
        dao.insertAlert(alert)
        dao.deleteAlert(alert)

        val allShoppingItems = dao.storedAllAlert().getOrAwaitValue()

        //MatcherAssert.assertThat(allShoppingItems).doesNotContain(alert)
    }
}
