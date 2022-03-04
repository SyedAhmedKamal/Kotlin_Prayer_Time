package com.example.kotlinprayertime.fragments

import android.Manifest
import android.content.Context
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
import com.example.kotlinprayertime.databinding.FragmentHijriCalendarBinding
import com.example.kotlinprayertime.utils.NetworkStatus
import com.example.kotlinprayertime.utils.Status
import com.example.kotlinprayertime.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class HijriCalendarFragment : Fragment() {

    private var _binding: FragmentHijriCalendarBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHijriCalendarBinding.inflate(inflater, container, false)
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

                        reAnimateViews()

                        binding.apply {

                            it.data.date.gregorian.apply {

                                tvDayGregorian.text = this.day
                                tvMonthGregorian.text = "/" + this.month.number
                                tvYearGregorian.text = "/" + this.year

                            }

                            it.data.date.hijri.apply {

                                tvDayHijri.text = this.day
                                tvMonthHijri.text = "/" + this.hijriMonth.number
                                tvYearHijri.text = "/" + this.year
                                arabicMonth.text = this.hijriMonth.ar

                            }
                        }
                    }
                }
            }
        }
    }

    private fun reAnimateViews() {

        binding.apply {

            tvDayGregorian.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvMonthGregorian.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvYearGregorian.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvDayHijri.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvMonthHijri.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            tvYearHijri.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

            arabicMonth.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )

        }

    }

    private fun checkLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE
                );
            } else {
                val locationManager =
                    activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    private fun alphaValueOFCardView() {
        binding.apply {

            cardGregorian.background.alpha = (0.4 * 255).toInt()
            cardHijri.background.alpha = (0.4 * 255).toInt()

        }
    }

    private fun animation() {

        binding.apply {

            divider.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.move_in))
            divider7.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.move_in))

            tvCalendarGregorian.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in
                )
            )

            tvCalendarHijri.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in
                )
            )

            cardGregorian.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in
                )
            )

            cardHijri.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_up_fade_in
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}