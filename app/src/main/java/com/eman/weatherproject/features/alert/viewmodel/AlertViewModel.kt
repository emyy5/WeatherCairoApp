package com.eman.weatherproject.features.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eman.weatherproject.ApiStateAlert
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel (private val repo: RepositoryInterface): ViewModel() {

  private  var stateOfAlert :MutableStateFlow<ApiStateAlert?> = MutableStateFlow(null)
    val alertState: StateFlow<ApiStateAlert?> =stateOfAlert

    fun getAllAlertInViewModel():LiveData<List<AlertData>> {
       return repo.getAllAlert()
    }

    fun addAlertToViewModel(alert: AlertData){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertMyAlert(alert)
        }
    }

    fun removeAlertFromViewModel(alert: AlertData){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteMyAlert(alert)
        }
    }


}
