package com.example.fridgehelper.products

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import com.example.fridgehelper.DB
import com.example.fridgehelper.Functions
import com.example.fridgehelper.Products
import com.example.fridgehelper.R
import java.util.*

class ProductObjAdder : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = AddProductObj::class.java
        val int = Intent(applicationContext, i)
        val name = intent.getStringExtra("name")
        val unit = intent.getStringExtra("unit")
        int.putExtra("name", name)
        int.putExtra("unit", unit)
        startActivity(int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_obj_adder)
        val calendar = findViewById<CalendarView>(R.id.calendarView)
        val quantity = findViewById<EditText>(R.id.quantityInput)
        val button = findViewById<Button>(R.id.buttonAddProductObj)
        val checkBox = findViewById<CheckBox>(R.id.checkBoxUnlimitedDate)
        val date = GregorianCalendar()
        val name = intent.getStringExtra("name")
        val unit = intent.getStringExtra("unit")

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date.set(year, month, dayOfMonth)
        }
        button.setOnClickListener(View.OnClickListener {
            if (quantity.text.toString() == "") {
                Toast.makeText(
                    this,
                    getString(R.string.no_quantity_product),
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }
            val d: Long
            if (checkBox.isChecked) {
                d = -1L
            } else d = date.time.time
            val db =
                DB.ProductDBAdapter(applicationContext, name).writableDatabase
            val contentValues = ContentValues()
            contentValues.put(DB.ProductDB.TABLE_QUANTITY, quantity.text.toString().toFloat())
            contentValues.put(DB.ProductDB.TABLE_DATE, d)
            db.insert(Functions.stringToSQLLite3(name.toString()), null, contentValues)
            val intent = Intent(this, AddProductObj::class.java)
            intent.putExtra("name", name)
            intent.putExtra("unit", unit)
            startActivity(intent)

        })
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1) {
                    calendar.visibility = View.INVISIBLE
                } else calendar.visibility = View.VISIBLE
            }

        })
    }
}