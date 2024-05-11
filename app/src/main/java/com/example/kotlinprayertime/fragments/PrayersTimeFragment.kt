package com.example.kotlinprayertime.fragments

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.*
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kotlinprayertime.R
import com.example.kotlinprayertime.databinding.FragmentPrayersTimeBinding
import com.example.kotlinprayertime.datamodel.MainContainer
import com.example.kotlinprayertime.utils.AlarmReceiver
import com.example.kotlinprayertime.utils.NetworkStatus
import com.example.kotlinprayertime.utils.Status
import com.example.kotlinprayertime.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class PrayersTimeFragment : Fragment() {

    private var _binding: FragmentPrayersTimeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    val SHARED_KEY = "com.example.kotlinprayertime.TIMING_KEY"
    private lateinit var sharedPref: SharedPreferences
    private val TAG = "PrayersTimeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPrayersTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation()
        alphaValueOFCardView()

        // Internet and network checking
        val networkStatus = NetworkStatus(requireContext())

        networkStatus.observe(viewLifecycleOwner) { isAvailable ->

            when (isAvailable) {

                true -> {
                    checkLocationPermission()
                    displayData()
                }

                false -> {
                    Snackbar.make(
                        this.requireView(),
                        "Internet is not available",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

            }

        }


    }

    private fun displayData() {

        mainViewModel.apiHandler.observe(this.viewLifecycleOwner) {

            when (it.status) {

                Status.SUCCESS -> {
                    it.data?.let {
                        Log.d("timingFragment", "displayData: " + it.data.timings.Fajr)
                        binding.apply {
                            reanimatetv()
                            updateUI(it)
                        }
                    }
                }
                Status.LOADING -> {
                    Log.d("timingFragment", "displayData: loading")
                }
                Status.ERROR -> {
                    Log.d("timingFragment", "displayData: " + it.message)
                }

            }

        }

    }

    private fun FragmentPrayersTimeBinding.updateUI(it: MainContainer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val result: String =
                LocalTime.parse(it.data.timings.Dhuhr, DateTimeFormatter.ofPattern("HH:mm"))
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))

            Log.d(TAG, "displayData: ${result}")

            fajarTiming(result)

            tvFajar.text =
                LocalTime.parse(it.data.timings.Fajr, DateTimeFormatter.ofPattern("HH:mm"))
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvDuhur.text =
                LocalTime.parse(it.data.timings.Dhuhr, DateTimeFormatter.ofPattern("HH:mm"))
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvAsar.text = LocalTime.parse(it.data.timings.Asr, DateTimeFormatter.ofPattern("HH:mm"))
                .format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvMaghrib.text =
                LocalTime.parse(it.data.timings.Maghrib, DateTimeFormatter.ofPattern("HH:mm"))
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvIsha.text =
                LocalTime.parse(it.data.timings.Isha, DateTimeFormatter.ofPattern("HH:mm"))
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvLocation.text = mainViewModel._city.value
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun fajarTiming(fajr: String) {

        val hour = fajr.substring(0, 2).toInt()
        val minute = fajr.substring(3, 5).toInt()

        sharedPref = activity?.getSharedPreferences(SHARED_KEY, MODE_PRIVATE)!!
        val sharedFajarHour = sharedPref.getInt("Fajar hour", 0)
        val sharedFajarMinute = sharedPref.getInt("Fajar minute", 0)

        Log.d(TAG, "SharedFajarTiming: ${sharedFajarHour}-${sharedFajarMinute}")
        Log.d(TAG, "DefaultFajarTiming: ${hour}-${minute}")

        if (fajr != null) {
            if (hour == sharedFajarHour && minute == sharedFajarMinute) {

                sharedPref = activity?.getSharedPreferences(SHARED_KEY, MODE_PRIVATE) ?: return
                with(sharedPref.edit()) {
                    putInt("Fajar hour", hour)
                    putInt("Fajar minute", minute)
                    apply()
                }

                Log.d(TAG, "fajarTiming: alarm is set")

                calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = 16
                calendar[Calendar.MINUTE] = 19
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
                val intent = Intent(this.context, AlarmReceiver::class.java)
                pendingIntent = PendingIntent.getBroadcast(
                    this.context,
                    100,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                // TODO: get alarm permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()){
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                }
                else{
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }

                Log.d(TAG, "ALARM: ${alarmManager.nextAlarmClock}")

            }
        }
    }

    private fun checkLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.let {
                    ContextCompat.checkSelfPermission(it, ACCESS_COARSE_LOCATION)
                } != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE);
            } else {
                val locationManager =
                    activity?.getSystemService(LOCATION_SERVICE) as LocationManager
                val location =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                location?.let {
                    reverseGeocoding(location.latitude, location.longitude)
                }
            }
        }

    }


    private fun reverseGeocoding(latitude: Double, longitude: Double) {

        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 10)

            if (addresses.size > 0) {
                for (adr in addresses) {
                    if (adr.locality != null && adr.locality.length > 0) {
                        Log.d("timing", "reverseGeocoding: " + adr.locality)
                        Log.d("timing", "reverseGeocoding: " + adr.countryName)
                        mainViewModel._city.value = adr.locality
                        mainViewModel._country.value = adr.countryName
                        break;
                    }
                }
            }
        } catch (e: IOException) {
            Log.d("geocode", "reverseGeocoding: " + e.message)
        }
    }

    private fun reanimatetv() {
        binding.apply {

            tvFajar.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvDuhur.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvAsar.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvMaghrib.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvIsha.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvLocation.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

        }
    }

    private fun alphaValueOFCardView() {

        binding.apply {

            card1.background.alpha = (1.0 * 255).toInt()
            card2.background.alpha = (0.8 * 255).toInt()
            card3.background.alpha = (0.6 * 255).toInt()
            card4.background.alpha = (0.4 * 255).toInt()
            card5.background.alpha = (0.2 * 255).toInt()

        }
    }

    private fun animation() {

        binding.apply {

            card1.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in
                )
            )
            tvFajar.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            card2.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in1
                )
            )
            tvDuhur.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            card3.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in2
                )
            )
            tvAsar.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            card4.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in3
                )
            )
            tvMaghrib.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            card5.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in4
                )
            )
            tvIsha.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvLocation.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}