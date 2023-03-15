package com.eman.weatherproject

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.eman.weatherproject.database.repository.Repository
import com.eman.weatherproject.database.room.LocalSource
import com.eman.weatherproject.features.alert.view.AlertManager
import com.eman.weatherproject.features.alert.viewmodel.AlertViewModel
import com.eman.weatherproject.features.alert.viewmodel.AlertViewModelFactory
import com.eman.weatherproject.utilities.SHARED_PREFERENCES
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var navigationDrawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var alertViewModel:AlertViewModel
    lateinit var toolbar: Toolbar
    lateinit var navController: NavController
    lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewModelFactory = AlertViewModelFactory( Repository(
            RemoteSource.getInstance(),
            LocalSource.getInstance(this),this,getSharedPreferences(
                SHARED_PREFERENCES, MODE_PRIVATE
            ))
        )
        alertViewModel = ViewModelProvider(this,viewModelFactory).get(AlertViewModel::class.java)

        navigationDrawer = findViewById(R.id.mainDrawer)
        navigationView = findViewById(R.id.mainNavView)
        toolbar = findViewById(R.id.mainToolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfig = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.favouriteFragment,
            R.id.settingFragment,
            R.id.alertsFragment2),navigationDrawer)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController,appBarConfig)
        navigationView.setupWithNavController(navController)

        activateAlerts()
    }

    private fun activateAlerts() {
        val alertsManager = AlertManager(this)
        lifecycleScope.launch {

                alertViewModel.getAllAlertInViewModel().observe(this@MainActivity) { it ->
                    it.forEach {

                        alertsManager.fireAlert(it)
                    }
                }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}
