package com.example.fridgehelper.products

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgehelper.Functions
import com.example.fridgehelper.Products
import com.example.fridgehelper.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddProductObj : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = Products::class.java
        startActivity(Intent(applicationContext, i))
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("name")
        val unit = intent.getStringExtra("unit")
        setContentView(R.layout.activity_add_product_obj)
        val arrayList = Functions.getProductDB(this, name.toString())
        //productQuantity
        val productQuantity = findViewById<TextView>(R.id.productQuantity)
        var quantity = 0f
        for (i in arrayList) {
            quantity += i.quantity
        }
        if (quantity == (0).toFloat()) {
            productQuantity.text = ""
        } else productQuantity.text = "$quantity$unit"
        //productName
        val productName = findViewById<TextView>(R.id.productName)
        productName.text = name
        //productDate
        val productDate = findViewById<TextView>(R.id.productDate)
        val addition = findViewById<TextView>(R.id.textViewAdditionView) //TODO добавить для addition поля
        if (Functions.findMinDate(arrayList) == (-1).toLong()) {
            productDate.text = ""
        } else productDate.text =
            Functions.dateOutput(Functions.longToCalendar(Functions.findMinDate(arrayList)))
        //addProductObj
        val addProductObj = findViewById<FloatingActionButton>(R.id.addProductObj)
        addProductObj.setOnClickListener {
            val intent = Intent(this, ProductObjAdder::class.java)
            intent.putExtra("name", name)
            intent.putExtra("unit", unit)
            startActivity(intent)
        }
        val productClickListener: ProductObjAdapter.OnProductObjClickListener =
            object : ProductObjAdapter.OnProductObjClickListener {
                override fun onProductClick(product: Functions.ProductData, position: Int) {
                    val intent = Intent(applicationContext, ProductObjEdit::class.java)
                    Toast.makeText(
                        applicationContext, "Quantity: ${product.quantity}, Date: ${
                            Functions.dateOutput(
                                Functions.longToCalendar(product.date)
                            )
                        }", Toast.LENGTH_LONG
                    ).show()
                    intent.putExtra("name", name)
                    intent.putExtra("quantity", product.quantity)
                    intent.putExtra("date", product.date)
                    intent.putExtra("unit", unit)
                    startActivity(intent)
                }
            }

        val recyclerView = findViewById<RecyclerView>(R.id.productObjRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProductObjAdapter(arrayList, productClickListener)
        recyclerView.adapter = adapter

    }
}