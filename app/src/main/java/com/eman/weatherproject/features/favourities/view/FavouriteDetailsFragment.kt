package com.eman.weatherproject.features.favourities.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.eman.weatherproject.R
import com.eman.weatherproject.database.network.RemoteSource
import com.eman.weatherproject.database.repository.Repository
import com.eman.weatherproject.database.room.LocalSource
import com.eman.weatherproject.databinding.FragmentFavouriteDetailsBinding
import com.eman.weatherproject.features.home.view.DailyAdapter
import com.eman.weatherproject.features.home.view.HourlyAdapter
import com.eman.weatherproject.features.home.viewmodel.HomeViewModel
import com.eman.weatherproject.features.home.viewmodel.HomeViewModelFactory
import com.eman.weatherproject.utilities.Converters
import com.eman.weatherproject.utilities.SHARED_PREFERENCES
import com.eman.weatherproject.utilities.units


class FavouriteDetailsFragment : Fragment() {

    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var detailsViewModel: HomeViewModel
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var layoutManagerHourly: LinearLayoutManager
    lateinit var layoutManagerDaily: LinearLayoutManager
    lateinit var binding:FragmentFavouriteDetailsBinding
    private lateinit var navController: NavController
    private var settings: com.eman.weatherproject.database.model.Settings? = null
    val weatherToShow:FavouriteDetailsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favourite_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteDetailsBinding.bind(view)
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)

        viewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                RemoteSource.getInstance(),
                LocalSource.getInstance(requireActivity()),
                requireContext(),
                requireContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)))
        detailsViewModel = ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)

        settings = detailsViewModel.getStoredSettings()
        setupRecyclerViews()

        binding.currCity.text = weatherToShow.weather.timezone
        binding.currDate.text = Converters.getDateFormat(weatherToShow.weather.current.dt)
        binding.currTime.text = Converters.getTimeFormat(weatherToShow.weather.current.dt)
        binding.currTemp.text = weatherToShow.weather.current.temp.toString()
        binding.currDesc.text = weatherToShow.weather.current.weather[0].description

        binding.currHumidity.text = weatherToShow.weather.current.humidity.toString()
        binding.currWindSpeed.text = weatherToShow.weather.current.wind_speed.toString()
        binding.currClouds.text = weatherToShow.weather.current.clouds.toString()
        binding.currPressure.text = weatherToShow.weather.current.pressure.toString()
        when(units[settings?.unit as Int]) {
            "standard" ->{
                binding.currUnit.text = getString(R.string.Kelvin)
                binding.currWindUnit.text = getString(R.string.windMeter)
            }
            "metric" ->{
                binding.currUnit.text = getString(R.string.Celsius)
                binding.currWindUnit.text = getString(R.string.windMeter)
            }
            "imperial" ->{
                binding.currUnit.text = getString(R.string.Fahrenheit)
                binding.currWindUnit.text = getString(R.string.windMile)
            }
        }

        Glide.with(context as Context)
            .load("https://openweathermap.org/img/wn/"+weatherToShow.weather.current.weather[0].icon+"@2x.png")
            .into(binding.currIcon)
        hourlyAdapter.setHourlyWeatherList(weatherToShow.weather.hourly)
        dailyAdapter.setDailyWeatherList(weatherToShow.weather.daily)

        hourlyAdapter.notifyDataSetChanged()
        dailyAdapter.notifyDataSetChanged()
        Log.i("TAG", "onViewCreated: finished")

    }

    fun setupRecyclerViews(){

        hourlyAdapter = HourlyAdapter(context as Context, arrayListOf(), units[settings?.unit as Int])
        dailyAdapter = DailyAdapter(context as Context, arrayListOf(),units[settings?.unit as Int])
        layoutManagerHourly = LinearLayoutManager(context as Context,
            LinearLayoutManager.HORIZONTAL,false)
        layoutManagerDaily = LinearLayoutManager(context as Context)
        binding.hourlyRecycler.adapter = hourlyAdapter
        binding.dailyRecycler.adapter = dailyAdapter
        binding.hourlyRecycler.layoutManager = layoutManagerHourly
        binding.dailyRecycler.layoutManager = layoutManagerDaily
    }

}
