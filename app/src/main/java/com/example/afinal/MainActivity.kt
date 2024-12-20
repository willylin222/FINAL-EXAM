package com.example.afinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.afinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bd : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bd.root)
    }
}