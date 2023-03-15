package com.eman.weatherproject.features.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eman.weatherproject.database.room.LocalSource
import com.eman.weatherproject.R
import com.eman.weatherproject.RemoteSource
import com.eman.weatherproject.database.model.Settings
import com.eman.weatherproject.database.model.WeatherAddress
import com.eman.weatherproject.features.favourities.viewmodel.FavoriteViewModelFactory
import com.eman.weatherproject.features.favourities.viewmodel.FavouriteViewModel
import com.eman.weatherproject.database.repository.Repository
import com.eman.weatherproject.features.home.view.HomeFragmentDirections
import com.eman.weatherproject.utilities.SHARED_PREFERENCES
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var favViewModelFactory: FavoriteViewModelFactory
    private lateinit var favViewModel: FavouriteViewModel
    private val fragmentType: MapsFragmentArgs by navArgs()
    private var settings: Settings? = null

    private val callback = OnMapReadyCallback { googleMap ->
        val ismailia = LatLng(30.6009763,32.2695462)
        googleMap.addMarker(MarkerOptions().position(ismailia).title("Ismailia"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ismailia,15f))

        googleMap.setOnMapClickListener{
            googleMap.clear()

            val someLocation = LatLng(it.latitude, it.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(someLocation,8f))

            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.saveLocation))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.save)) { dialog, id ->
                    val latIn4Digits: Double
                    val lonIn4Digits: Double
                    if(settings?.language as Boolean) {
                        latIn4Digits = String.format("%.4f", it.latitude).toDouble()
                        lonIn4Digits = String.format("%.4f", it.longitude).toDouble()
                    } else {
                        latIn4Digits = it.latitude
                        lonIn4Digits = it.longitude
                    }
                    val selectedAddress = WeatherAddress("addressName", latIn4Digits, lonIn4Digits)
                    addWeatherWithAddress(selectedAddress)


                    findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToHomeFragment(
                        latIn4Digits.toFloat(),
                        lonIn4Digits.toFloat(),
                        "standard",
                        true
                    ))
                    if(fragmentType.comeFromAlert){

                        val action = HomeFragmentDirections.actionHomeFragmentToAlertsFragment2(it.latitude.toFloat(),it.longitude.toFloat())
                            navController.navigate(action)
                    }


                    dialog.cancel()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, id -> dialog.cancel()}
            val alert = dialogBuilder.create()
            alert.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        navController = Navigation.findNavController(activity as AppCompatActivity,
            R.id.nav_host_fragment
        )

        favViewModelFactory = FavoriteViewModelFactory(
            Repository.getInstance(RemoteSource.getInstance(),
                LocalSource.getInstance(requireActivity()),
                requireContext(), requireContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            )
        )
        favViewModel = ViewModelProvider(this,favViewModelFactory).get(FavouriteViewModel::class.java)

        settings = favViewModel.getStoredSettings()

        mapFragment?.getMapAsync(callback)

    }

    fun getAddressFromLatLng(lat:Double,longg:Double) : String{
        val geocoder = Geocoder(context as Context, Locale.getDefault())
        val addresses:List<Address>

        addresses = geocoder.getFromLocation(lat,longg,1) as List<Address>
        if(addresses.size>0) {
            return addresses.get(0).getAddressLine(0)
        }
        return ""
    }

    fun addWeatherWithAddress(address: WeatherAddress){
        favViewModel.addAddressToFavorites(address)
    }
}