package com.example.kotlinprayertime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.kotlinprayertime.R
import com.example.kotlinprayertime.databinding.FragmentHijriCalendarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HijriCalendarFragment : Fragment() {

    private var _binding: FragmentHijriCalendarBinding? = null
    private val binding get() = _binding!!

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