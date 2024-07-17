package com.example.fridgehelper

import com.example.fridgehelper.service.CheckProductsDate
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, CheckProductsDate::class.java))
        val buttonProducts = findViewById<Button>(R.id.button_products)
        val buttonRecipes = findViewById<Button>(R.id.button_recipes)
        val buttonList = findViewById<Button>(R.id.button_list)
        buttonProducts.setOnClickListener(View.OnClickListener {
            val intentProducts = Intent(this, Products::class.java)
            startActivity(intentProducts)
        })
        buttonRecipes.setOnClickListener {
            val intentRecipes = Intent(this, Recipes::class.java)
            startActivity(intentRecipes)
        }
        buttonList.setOnClickListener {
            val intentList = Intent(this,com.example.fridgehelper.List::class.java)
            startActivity(intentList)
        }

    }
}