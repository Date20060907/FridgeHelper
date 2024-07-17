package com.example.fridgehelper.products

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.fridgehelper.DB
import com.example.fridgehelper.Functions
import com.example.fridgehelper.Products
import com.example.fridgehelper.R

class ProductObjEdit : AppCompatActivity() {
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
        setContentView(R.layout.activity_product_obj_edit)
        val name = intent.getStringExtra("name")
        val date = intent.getLongExtra("date", 0)
        val quantity = intent.getFloatExtra("quantity", 0f)
        val db = DB.ProductDBAdapter(this, Functions.stringToSQLLite3(name.toString())).writableDatabase
        val number = findViewById<EditText>(R.id.editTextNumberDecimal)
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            db.delete(
                Functions.stringToSQLLite3(name.toString()),
                "${DB.ProductDB.TABLE_DATE} = $date AND ${DB.ProductDB.TABLE_QUANTITY} = $quantity",
                null
            )
            val int = Intent(this, AddProductObj::class.java)
            int.putExtra("name", name)
            int.putExtra("unit", intent.getStringExtra("unit"))
            startActivity(int)
        }
        val plusButton = findViewById<Button>(R.id.plusButton)
        plusButton.setOnClickListener {
            val cv = ContentValues()
            cv.put(DB.ProductDB.TABLE_QUANTITY, quantity + number.text.toString().toFloat())
            cv.put(DB.ProductDB.TABLE_DATE, date)
            db.update(
                Functions.stringToSQLLite3(name.toString()),
                cv,
                "${DB.ProductDB.TABLE_DATE} = $date AND ${DB.ProductDB.TABLE_QUANTITY} = $quantity",
                null
            )
            val int = Intent(this, AddProductObj::class.java)
            int.putExtra("name", name)
            int.putExtra("unit", intent.getStringExtra("unit"))
            startActivity(int)
        }
        val minusButton = findViewById<Button>(R.id.minusButton)
        minusButton.setOnClickListener {
            val cv = ContentValues()
            cv.put(DB.ProductDB.TABLE_QUANTITY, quantity - number.text.toString().toFloat())
            cv.put(DB.ProductDB.TABLE_DATE, date)
            db.update(
                Functions.stringToSQLLite3(name.toString()),
                cv,
                "${DB.ProductDB.TABLE_DATE} = $date AND ${DB.ProductDB.TABLE_QUANTITY} = $quantity",
                null
            )
            val int = Intent(this, AddProductObj::class.java)
            int.putExtra("name", name)
            int.putExtra("unit", intent.getStringExtra("unit"))
            startActivity(int)
        }
    }
}