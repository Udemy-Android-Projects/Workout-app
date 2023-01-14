package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.workoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flStart?.setOnClickListener{
            startActivity(Intent(this,ExerciseActivity::class.java))
        }
        binding?.flBMI?.setOnClickListener{
            startActivity(Intent(this,BMI::class.java))
        }
        binding?.flHistory?.setOnClickListener{
            startActivity(Intent(this,HistoryActivity::class.java))
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        binding = null
    }
}