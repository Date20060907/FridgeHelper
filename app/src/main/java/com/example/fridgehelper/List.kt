package com.example.fridgehelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class List : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = MainActivity::class.java
        startActivity(Intent(applicationContext, i))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}