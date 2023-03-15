package com.eman.weatherproject.features.favourities.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.eman.weatherproject.R
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.database.model.WeatherForecast
import com.eman.weatherproject.database.room.FavClickInterface

class FavouroiteAdapter : RecyclerView.Adapter<FavouroiteAdapter.FavoriteViewHolder> {

    private var context: Context
    private var favAddresses: List<WeatherAddress>
    private var favWeatherList: List<WeatherForecast>
    private var favClick: FavClickInterface
    lateinit var removeFav: (WeatherAddress)


    constructor(
        context: Context,
        favAddresses: List<WeatherAddress>,
        favWeatherList: List<WeatherForecast>,
        onClickHandler: FavClickInterface
    ) {
        this.context = context
        this.favAddresses = favAddresses
        this.favWeatherList = favWeatherList
        this.favClick = onClickHandler
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val address = favAddresses[position]
        holder.addressName.text =getAddressEnglish(context,favAddresses[position].lat,favAddresses[position].lon)
        holder.removeFav.setOnClickListener {
            if (favAddresses.size<=position)
                 favClick.onRemoveBtnClick(address, favWeatherList[position])

        }
        holder.favConstraint.setOnClickListener {
            favClick.onFavItemClick(address)
        }

    }

    override fun getItemCount(): Int {
        return favAddresses.size
    }
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{

        var address:MutableList<Address>?=null

        val geocoder= Geocoder(context)
        address =geocoder.getFromLocation(lat!!,lon!!,1)
        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }

    fun setFavAddressesList(addressList: List<WeatherAddress>) {
        this.favAddresses = addressList
    }

    fun setFavWeatherList(weatherList: List<WeatherForecast>) {
        this.favWeatherList = weatherList
    }


    inner class FavoriteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val favConstraint: ConstraintLayout
            get() = view.findViewById(R.id.favConstraint)
        val addressName: TextView
            get() = view.findViewById(R.id.favAddressName)
         val removeFav: ImageView get() = view.findViewById(R.id.removeFav)


    }
}