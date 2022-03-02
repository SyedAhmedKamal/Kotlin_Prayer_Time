package com.example.kotlinprayertime

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinprayertime.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarInside)

        CoroutineScope(Dispatchers.IO).launch {
            animation()
            alphaValueOFCardView()
        }
    }

    private fun alphaValueOFCardView() {
        binding.apply {

            card1.background.alpha = (0.4 * 255).toInt()
            card2.background.alpha = (0.4 * 255).toInt()
            card3.background.alpha = (0.4 * 255).toInt()
            card4.background.alpha = (0.4 * 255).toInt()
            card5.background.alpha = (0.4 * 255).toInt()

        }
    }

    private fun animation() {

        binding.apply {

            card1.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_up_fade_in
                )
            )
            tvFajar.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.fade_in
                )
            )

            card2.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_up_fade_in1
                )
            )
            tvDuhur.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.fade_in
                )
            )

            card3.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_up_fade_in2
                )
            )
            tvAsar.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.fade_in
                )
            )

            card4.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_up_fade_in3
                )
            )
            tvMaghrib.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.fade_in
                )
            )

            card5.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_up_fade_in4
                )
            )
            tvIsha.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.fade_in
                )
            )

            tvLocation.startAnimation(
                AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.fade_in
                )
            )
        }
    }
}