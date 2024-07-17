package com.example.fridgehelper

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgehelper.products.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


class Products : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = MainActivity::class.java
        startActivity(Intent(applicationContext, i))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerViewProducts)
        val addProductButton =
            findViewById<FloatingActionButton>(R.id.floatingActionButtonAddProduct)
        val spinner = findViewById<Spinner>(R.id.spinnerSorting)

        //recyclerView

        var arrayList = ArrayList<Product>()
        val names = Functions.getProductListArrayFromDB(this)

        for (i in names) {
            val values = Functions.getProductDB(this, i.name)
            arrayList.add(
                Product(
                    i.name,
                    resources.getStringArray(R.array.units)[i.unit],
                    Functions.longToCalendar(Functions.findMinDate(values)),
                    Functions.sumQuantity(values)
                )
            )
        }
        val productClickListener: ProductAdapter.OnProductClickListener =
            object : ProductAdapter.OnProductClickListener {
                override fun onProductClick(product: Product, position: Int) {
                    val intent = Intent(applicationContext, AddProductObj::class.java)
                    intent.putExtra("name", product.title)
                    intent.putExtra("unit", product.unit)
                    startActivity(intent)
                }
            }
        val onLongClickListener = object : ProductAdapter.OnProductLongClickListener {
            override fun onLongProductClick(product: Product, position: Int) {
                val intent = Intent(applicationContext, ProductEdit::class.java)
                intent.putExtra("name", product.title)
                intent.putExtra("unit", product.unit)
                startActivity(intent)
            }
        }

        fun sortArray(sort_type: Int) {
            when (sort_type) {
                0 -> {
                    arrayList.sortBy { it.title }
                }
                1 -> {
                    arrayList.sortBy { it.date }
                }

            }
        }
        sortArray(spinner.selectedItemPosition)


        //addProduct
        addProductButton.setOnClickListener(View.OnClickListener {
            val addProductIntent = Intent(this, AddProduct::class.java)
            startActivity(addProductIntent)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter = ProductAdapter(arrayList, productClickListener, onLongClickListener)
        recyclerView.adapter = adapter

        spinner.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sortArray(p2)
                adapter = ProductAdapter(arrayList, productClickListener, onLongClickListener)
                recyclerView.adapter = adapter
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        })


    }
}
