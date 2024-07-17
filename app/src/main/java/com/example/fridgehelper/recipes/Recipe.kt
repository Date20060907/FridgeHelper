package com.example.fridgehelper.recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.example.fridgehelper.Functions
import com.example.fridgehelper.R
import com.example.fridgehelper.Recipes
import java.math.RoundingMode

class Recipe : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = Recipes::class.java
        startActivity(Intent(applicationContext, i))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val name = intent.getStringExtra("name")
        val guide = intent.getStringExtra("guide")
        val recipeName = findViewById<TextView>(R.id.recipeNameTextView)
        val recipeGuide = findViewById<TextView>(R.id.guideTextView)
        val num = findViewById<EditText>(R.id.recipeEditTextNumber)
        val recipeProducts = findViewById<TextView>(R.id.productsTextView)
        val products = Functions.getRecipeProductsListArrayFromDB(applicationContext, name)
        if (name != null) {
            recipeName.text = name.uppercase().replace("_", " ")
        }
        if (guide != null) {
            recipeGuide.text = guide.replace("\"", "")
        }

        var productsString: String = ""
        for (i in products) {
            if (productsString == "") {
                productsString += "${i.name} ${
                    (i.quantity * num.text.toString().toFloat()).toBigDecimal().setScale(2, RoundingMode.UP)
                }${resources.getStringArray(R.array.units)[i.unit]}"
                continue
            }
            productsString += ", ${i.name} ${
                (i.quantity * num.text.toString().toFloat()).toBigDecimal().setScale(2, RoundingMode.UP)
            }${resources.getStringArray(R.array.units)[i.unit]}"
        }
        recipeProducts.text = productsString

        num.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p0 != null) {
                    productsString = ""
                    for (i in products) {
                        if (productsString == "") {
                            productsString += "${i.name} ${
                                (i.quantity * p0.text.toString().toFloat()).toBigDecimal().setScale(2, RoundingMode.UP)
                            }${resources.getStringArray(R.array.units)[i.unit]}"
                            continue
                        }
                        productsString += ", ${i.name} ${
                            (i.quantity * p0.text.toString().toFloat()).toBigDecimal().setScale(2, RoundingMode.UP)
                        }${resources.getStringArray(R.array.units)[i.unit]}"
                    }
                    recipeProducts.text = productsString

                }
                return true
            }

        })

    }
}