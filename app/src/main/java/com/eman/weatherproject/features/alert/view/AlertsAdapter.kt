package com.eman.weatherproject.features.alert.view

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.room.AlertListenerInterface
import com.eman.weatherproject.databinding.AlertItemLayoutBinding
import kotlinx.coroutines.CoroutineScope

class AlertsAdapter (private val lifeCycleScopeInput: CoroutineScope, var onClickHandler: AlertListenerInterface
)
    : ListAdapter<AlertData, AlertsAdapter.AlertViewHolder>(AlertDiffUtil()) {
    class AlertViewHolder(var binding: AlertItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    class AlertDiffUtil : DiffUtil.ItemCallback<AlertData>() {
        override fun areItemsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
            return (oldItem.idHashLongFromLonLatStartStringEndStringAlertType == newItem.idHashLongFromLonLatStartStringEndStringAlertType)
        }

        override fun areContentsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
            return oldItem == newItem
        }

    }

    lateinit var binding: AlertItemLayoutBinding

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.context)

        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertItemLayoutBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.fromTimeAlert.text = current.address


        holder.binding.removeAlert.setOnClickListener {
            onClickHandler.removeAlertClick(current)
        }

        holder.binding.fromDateAlert.text = current.startString


    }
}


