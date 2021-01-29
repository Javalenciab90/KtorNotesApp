package com.java90.ktornotesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}