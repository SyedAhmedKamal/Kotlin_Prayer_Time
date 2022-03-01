package com.example.kotlinprayertime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.kotlinprayertime.databinding.ActivityMainBinding
import com.example.kotlinprayertime.databinding.TimingLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tmBinding: TimingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            animation()
        }
    }

    private fun animation() {
        binding.card1.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_up_fade_in
            )
        )

        binding.card2.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_up_fade_in1
            )
        )

        binding.card3.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_up_fade_in2
            )
        )

        binding.card4.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_up_fade_in3
            )
        )

        binding.card5.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_up_fade_in4
            )
        )
    }
}