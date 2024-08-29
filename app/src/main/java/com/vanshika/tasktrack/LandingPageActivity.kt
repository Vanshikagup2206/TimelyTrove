package com.vanshika.tasktrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vanshika.tasktrack.databinding.ActivityLandingPageBinding
import com.vanshika.tasktrack.databinding.ActivityMainBinding

class LandingPageActivity : AppCompatActivity() {
    var binding : ActivityLandingPageBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        binding?.btnContinue?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}