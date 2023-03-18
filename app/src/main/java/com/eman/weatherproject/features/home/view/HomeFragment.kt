package com.eman.weatherproject.features.home.view

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.eman.weatherproject.ApiState
import com.eman.weatherproject.database.room.LocalSource
import com.eman.weatherproject.features.home.viewmodel.HomeViewModel
import com.eman.weatherproject.features.home.viewmodel.HomeViewModelFactory
import com.eman.weatherproject.R
import com.eman.weatherproject.database.network.RemoteSource
import com.eman.weatherproject.database.model.Settings
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.databinding.FragmentHomeBinding
import com.eman.weatherproject.database.repository.Repository
import com.eman.weatherproject.utilities.Converters
import com.eman.weatherproject.utilities.SHARED_PREFERENCES
import com.eman.weatherproject.utilities.units
import kotlinx.coroutines.launch

private const val TAG = "HFragment"
class HomeFragment : Fragment() {
    lateinit var loadingAnimation: LottieAnimationView
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var adapterDaily: DailyAdapter
    lateinit var layoutManagerHourly: LinearLayoutManager
    lateinit var layoutManagerDaily: LinearLayoutManager
    lateinit var binding: FragmentHomeBinding
    var info: NetworkInfo? = null
    lateinit var adapterHour: HourlyAdapter
    var connectivity: ConnectivityManager? = null
    val myArgs: HomeFragmentArgs by navArgs()
    private var settings: Settings? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        viewModelFactory = HomeViewModelFactory(Repository.getInstance(RemoteSource.getInstance(), LocalSource.getInstance(requireActivity()), requireActivity(), requireActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)))
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        loadingAnimation = view.findViewById(R.id.animationView)
        settings = viewModel.getStoredSettings()

        setupRecyclerViews()

        if (viewModel.getStoredCurrentWeather() == null || myArgs.comeForm) {
            viewModel.getWholeWeather(
                myArgs.lat.toDouble(),
                myArgs.loong.toDouble(),
                myArgs.unit
            )

            lifecycleScope.launch {
                viewModel.weatherFromNetwork.collect {


                    when (it) {
                        is ApiState.onSuccess -> {
                            viewModel.addWeatherInVM(it.weatherForecast)
                            adapterHour.notifyDataSetChanged()
                            adapterDaily.notifyDataSetChanged()
                        }
                        is ApiState.Loading -> {

                        }
                        is ApiState.onFail -> {

                        }

                        else -> {}
                    }
                }

            }

        } else {


            connectivity = context?.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                info = connectivity!!.activeNetworkInfo

                if (info != null) {
                    if (info!!.state == NetworkInfo.State.CONNECTED) {

                        viewModel.updateWeatherPrefs(this)
                    }
                } else {
                    Log.i("TAG", "no infooooooooo" )
                }
            }
            applyUIChange(viewModel.getStoredCurrentWeather())

            adapterHour.notifyDataSetChanged()
            adapterDaily.notifyDataSetChanged()
        }
        lifecycleScope.launch {
            applyUIChange(
                viewModel.repo.getCurrentWetherInRepo(myArgs.lat.toDouble(), myArgs.loong.toDouble(), "metric")
            )

            adapterHour.notifyDataSetChanged()
            adapterDaily.notifyDataSetChanged()
        }



    }

    private fun setupRecyclerViews() {
        adapterHour = HourlyAdapter(context as Context, arrayListOf(), "metric")
        adapterDaily = DailyAdapter(context as Context, arrayListOf(), "metric")
        layoutManagerHourly =
            LinearLayoutManager(context as Context, LinearLayoutManager.HORIZONTAL, false)
        layoutManagerDaily = LinearLayoutManager(context as Context)
        binding.hourlyRecycler.adapter = adapterHour
        binding.dailyRecycler.adapter = adapterDaily
        binding.hourlyRecycler.layoutManager = layoutManagerHourly
        binding.dailyRecycler.layoutManager = layoutManagerDaily
    }


    private fun applyUIChange(currWeather: WeatherForecast?) {
        currWeather as WeatherForecast
        loadingAnimation.visibility = View.GONE
        binding.currCity.text = currWeather.timezone
        binding.currDate.text = Converters.getDateFormat(currWeather.current.dt)
        binding.currTime.text = Converters.getTimeFormat(currWeather.current.dt)
        binding.currTemp.text = currWeather.current.temp.toString()
        binding.currDesc.text = currWeather.current.weather[0].description
        binding.currHumidity.text = currWeather.current.humidity.toString()
        binding.currWindSpeed.text = currWeather.current.wind_speed.toString()
        binding.currClouds.text = currWeather.current.clouds.toString()
        binding.currPressure.text = currWeather.current.pressure.toString()

        when(units[settings?.unit as Int]) {
            "standard" -> {
                binding.currUnit.text = getString(R.string.Kelvin)
                binding.currWindUnit.text = getString(R.string.windMeter)
            }
            "metric" -> {
                binding.currUnit.text = getString(R.string.Celsius)
                binding.currWindUnit.text = getString(R.string.windMeter)
            }
            "imperial" -> {
                binding.currUnit.text = getString(R.string.Fahrenheit)
                binding.currWindUnit.text = getString(R.string.windMile)
            }
        }
        Glide.with(context as Context)
            .load("https://openweathermap.org/img/wn/" + currWeather.current.weather[0].icon + "@2x.png")
            .into(binding.currIcon)
        adapterHour.setHourlyWeatherList(currWeather.hourly)
        adapterDaily.setDailyWeatherList(currWeather.daily)
    }
}








