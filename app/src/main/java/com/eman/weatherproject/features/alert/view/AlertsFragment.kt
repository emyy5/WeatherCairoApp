package com.eman.weatherproject.features.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eman.weatherproject.R
import com.eman.weatherproject.RemoteSource
import com.eman.weatherproject.database.model.AlertData
import com.eman.weatherproject.database.repository.Repository
import com.eman.weatherproject.database.room.LocalSource
import com.eman.weatherproject.database.room.AlertListenerInterface
import com.eman.weatherproject.databinding.FragmentAlertsBinding
import com.eman.weatherproject.features.alert.viewmodel.AlertViewModel
import com.eman.weatherproject.features.alert.viewmodel.AlertViewModelFactory
import com.eman.weatherproject.utilities.Converters
import com.eman.weatherproject.utilities.LocaleHelper
import com.eman.weatherproject.utilities.SHARED_PREFERENCES
import kotlinx.coroutines.*
import java.util.*


class AlertsFragment : Fragment(),AlertListenerInterface {




    private  val TAG = "AlertFragment"
    lateinit var binding: FragmentAlertsBinding
    lateinit var alertsAdapter: AlertsAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    lateinit var builder: AlertDialog.Builder
    lateinit var dialogView: View
    lateinit var alertDialog: AlertDialog
    lateinit var viewModel: AlertViewModel
    lateinit var viewModelFactory: AlertViewModelFactory
    val args :AlertsFragmentArgs by navArgs()


    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        viewModelFactory = AlertViewModelFactory( Repository(
            RemoteSource.getInstance(),
            LocalSource.getInstance(requireContext()),requireContext(),requireContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE))
        )
        viewModel = ViewModelProvider(this,viewModelFactory).get(AlertViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFrag()
        activateFABAlerts()
        setupAlertsAdapter()


    }


    private fun initFrag() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferencesEditor = sharedPreferences.edit()

        builder = AlertDialog.Builder(activity)
        dialogView = layoutInflater.inflate(R.layout.add_alert_dialog, null)

    }

    private fun activateFABAlerts() {
        binding.fabAlerts.setOnClickListener {

//            if (ContextCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//
//                val builder = AlertDialog.Builder(requireContext())
//
//                builder.setMessage("This app needs permission to show notifications. Please enable it in the app's settings.")
//                    .setTitle("Permission required")
//
//                builder.setPositiveButton("OK") { _, _ ->
//                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
//                    intent.data = uri
//                    startActivity(intent)
//                }
//
//                val dialog = builder.create()
//                dialog.show()
//            }

            if (!android.provider.Settings.canDrawOverlays(requireContext())) {

                val builder = AlertDialog.Builder(requireContext())

                builder.setMessage("This app needs permission to display an alarm over other apps. Please enable it in the app's settings.")
                    .setTitle("Permission required")

                builder.setPositiveButton("OK") { _, _ ->
                    val intent = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.data = Uri.parse("package:" + requireActivity().packageName)
                    startActivityForResult(intent, 0)
                }

                val dialog = builder.create()
                dialog.show()
            }

//            if ((ContextCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED
//                        ) && (android.provider.Settings.canDrawOverlays(requireContext()))
//            ) {
//
//            }

            showAddAlertDialog();
        }
    }

    private fun showAddAlertDialog() {

        (dialogView.parent as ViewGroup?)?.removeView(dialogView)

        builder.setView(dialogView)
        val textViewStartDate = dialogView.findViewById<TextView>(R.id.tv_start_date)
        // val textViewEndDate = dialogView.findViewById<TextView>(R.id.tv_end_date)
        val tvLocation = dialogView.findViewById<TextView>(R.id.tv_location)
        textViewStartDate.setOnClickListener { showDatePicker(textViewStartDate) }
        // textViewEndDate.setOnClickListener { showDatePicker(textViewEndDate) }
        tvLocation.setOnClickListener { tvLocationClicked() }
        builder.setPositiveButton("Save") { _, i ->
            if (textViewStartDate.text.toString().equals("start")) {
                Toast.makeText(requireContext(), getString(R.string.date_empty), Toast.LENGTH_SHORT).show()
            }else{
                saveClicked(textViewStartDate, textViewStartDate)

            }

        }
        builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }

        alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.rgb(29,41,86)))
        // alertDialog.window?.setBackgroundDrawable(Drawable.createFromPath("@drawable/rounded_corners"))

        alertDialog.setOnShowListener(DialogInterface.OnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        })
        alertDialog.show()


    }


    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { datePicker, year, month, day ->
                val date = day.toString() + " " + NativeDate.getMonth(month) + ", " + year
                showTimePicker(textView, date)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }
    private fun showTimePicker(textView: TextView, date: String) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            activity,
            { timePicker, hourOfDay, minute ->
                val time = "$hourOfDay:$minute"
                val dateTime = "$date $time"
                textView.text = dateTime
            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], false
        )
        timePickerDialog.show()
    }
    class NativeDate {
        companion object {
            fun getMonth(month: Int): String {
                val months = arrayOf(
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December"
                )
                return months[month]
            }
        }
    }

    private fun tvLocationClicked() {
        sharedPreferencesEditor.putBoolean("tvLocationClicked", true)
        sharedPreferencesEditor.apply()

        val action = AlertsFragmentDirections.actionAlertsFragment2ToMapsFragment(comeFromAlert =true)
        navController.navigate(action)


    }

    private fun saveClicked(textViewStartDate: TextView, textViewEndDate: TextView) {
        if (dialogView.findViewById<TextView>(R.id.tv_location).text.isNotEmpty() && textViewStartDate.text.isNotEmpty() && textViewEndDate.text.isNotEmpty() && (dialogView.findViewById<RadioButton>(
                R.id.rbNotification
            ).isChecked || dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked)
        ) {


            val dateStart = Converters.getDateFormattwo(textViewStartDate.text.toString())
            val unixTimeDTStart = dateStart?.time?.div(1000)

            lifecycleScope.launch {
                if (unixTimeDTStart != null
                //&& unixTimeDTEnd != null
                ) {

                    var alertType = ""
                    if (dialogView.findViewById<RadioButton>(R.id.rbNotification).isChecked) {
                        alertType = "notification"
                    } else if (dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked) {
                        alertType = "alarm"
                    }

                    val alertItem = AlertData(
                        address = LocaleHelper.addressFromLatitudeAndLongitdue(requireContext(),
                          args.lat.toDouble(),args.lon.toDouble()),
                        latitudeString = args.lat.toString()
                        ,
                        longitudeString = args.lon.toString()
                        ,
                        startString = textViewStartDate.text.toString(),
                        endString = textViewEndDate.text.toString(),
                        startDT = unixTimeDTStart.toInt(),
                        endDT = 11
                        ,
                        idHashLongFromLonLatStartStringEndStringAlertType = (

                                args.lat.toString()+args.lon.toString()
                                        + textViewStartDate.text.toString() + textViewEndDate.text.toString() + alertType


                                ).hashCode()
                            .toLong(),
                        alertType = alertType
                    )
                    // insert in repo

                    viewModel.addAlertToViewModel(alertItem)



                }
            }

        } else {
            Log.i(TAG, "saveClicked: not all fields are clicked")
        }
    }

    private fun setupAlertsAdapter() {
        val mlayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        alertsAdapter = AlertsAdapter(lifecycleScope,this)

        binding.rvAlerts.apply {
            layoutManager = mlayoutManager
            adapter = alertsAdapter
        }

        //get all alertFrom Repo
        viewModel.getAllAlertInViewModel().observe(viewLifecycleOwner) {
            alertsAdapter.submitList(it)
        }





    }

    override fun onPause() {

        super.onPause()
        if (sharedPreferences.getBoolean("tvLocationClicked", false)) {

            sharedPreferencesEditor.putBoolean("tvLocationClicked", false)

            sharedPreferencesEditor.putString(
                "start_date",
                dialogView.findViewById<TextView>(R.id.tv_start_date).text.toString()
            )
            sharedPreferencesEditor.putString(
                "end_date",
                "s"
            )
            sharedPreferencesEditor.putString(
                "ALERT_ADDRESS",
                dialogView.findViewById<TextView>(R.id.tv_location).text.toString()
            )

            var alartTypeSelected = ""

            if (dialogView.findViewById<RadioButton>(R.id.rbNotification).isChecked) {
                alartTypeSelected = "notification"
            } else if (dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked) {
                alartTypeSelected = "alarm"
            }

            sharedPreferencesEditor.putString("alarm_type_selected", alartTypeSelected)

            sharedPreferencesEditor.apply()

            alertDialog.dismiss()
        }

    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Log.i(TAG, "onViewStateRestored: ")

        if (sharedPreferences.getString("start_date", "")!!
                .isNotEmpty() || sharedPreferences.getString("end_date", "")!!
                .isNotEmpty() || sharedPreferences.getString("alarm_type_selected", "")!!
                .isNotEmpty() || sharedPreferences.getString("ALERT_ADDRESS", "")!!.isNotEmpty()
        ) {
            showAddAlertDialog()
            if (sharedPreferences.getString("start_date", "")!!.isNotEmpty()) {
                sharedPreferences.getString("start_date", "")
            }
            if (sharedPreferences.getString("end_date", "")!!.isNotEmpty()) {
            }
            if (sharedPreferences.getString("ALERT_ADDRESS", "")!!.isNotEmpty()) {
                dialogView.findViewById<TextView>(R.id.tv_location).text =
                    sharedPreferences.getString("ALERT_ADDRESS", "")
            }
            if (sharedPreferences.getString("alarm_type_selected", "") == "notification") {
                dialogView.findViewById<RadioButton>(R.id.rbNotification).isChecked = true
            } else if (sharedPreferences.getString("alarm_type_selected", "") == "alarm") {
                dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked = true
            }



            sharedPreferencesEditor.putString("start_date", "")
            sharedPreferencesEditor.putString("end_date", "")
            sharedPreferencesEditor.putString("alarm_type_selected", "")
            sharedPreferencesEditor.putString("ALERT_ADDRESS", "")

            sharedPreferencesEditor.apply()


        }

    }

    override fun removeAlertClick(alert: AlertData) {
        viewModel.removeAlertFromViewModel(alert)
    }

}
