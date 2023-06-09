package com.eman.weatherproject.features.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eman.weatherproject.database.model.HourlyWeather
import com.eman.weatherproject.R
import com.eman.weatherproject.utilities.Converters


class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyWeatherViewHolder> {

    private var context: Context
    private var WeatherHours: List<HourlyWeather>
    private val tempUnit: String

    constructor(context: Context, hourlyWeather: List<HourlyWeather>, tempUnit: String) {
        this.context = context
        this.WeatherHours = hourlyWeather
        this.tempUnit = tempUnit
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyWeatherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_item_layout, parent, false)
        return HourlyWeatherViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HourlyWeatherViewHolder,
        position: Int
    ) {
        val oneHourlyWeather: HourlyWeather = WeatherHours[position]
        holder.hourlyTime.text = Converters.getTimeFormat(oneHourlyWeather.dt)
        holder.hourlyTemp.text = oneHourlyWeather.temp.toString()
        holder.hourlyWindSpeed.text = oneHourlyWeather.wind_speed.toString()
        when (this.tempUnit) {
            "standard" -> {
                holder.tempUnit.text = context.getString(R.string.Kelvin)
                holder.windUnit.text = context.getString(R.string.windMeter)
            }
            "metric" -> {
                holder.tempUnit.text = context.getString(R.string.Celsius)
                holder.windUnit.text = context.getString(R.string.windMeter)
            }
            "imperial" -> {
                holder.tempUnit.text = context.getString(R.string.Fahrenheit)
                holder.windUnit.text = context.getString(R.string.windMile)
            }
        }
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + oneHourlyWeather.weather[0].icon + "@2x.png")
            .into(holder.hourlyIcon)
        //holder.hourlyIcon.setImageResource(R.drawable.hourly_icon)
    }

    override fun getItemCount(): Int {
        return WeatherHours.size - 24
    }

    fun setHourlyWeatherList(hourlyWeatherList: List<HourlyWeather>) {
        this.WeatherHours = hourlyWeatherList
    }

    inner class HourlyWeatherViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val hourlyTime: TextView
            get() = view.findViewById(R.id.time_h)
        val hourlyTemp: TextView
            get() = view.findViewById(R.id.temp_h)
        val hourlyWindSpeed: TextView
            get() = view.findViewById(R.id.windSpeed_h)
        val hourlyIcon: ImageView
            get() = view.findViewById(R.id.Icon_h)
        val tempUnit: TextView
            get() = view.findViewById(R.id.tempUnit_h)
        val windUnit: TextView
            get() = view.findViewById(R.id.hourlyWindUnit)
    }

}