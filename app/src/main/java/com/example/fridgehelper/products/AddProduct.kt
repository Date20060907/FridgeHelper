package com.example.fridgehelper.products

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.fridgehelper.*

class AddProduct : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = Products::class.java
        startActivity(Intent(applicationContext, i))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        val nameInput = findViewById<EditText>(R.id.NameInput)
        val unitInput = findViewById<Spinner>(R.id.UnitInput)
        val button = findViewById<Button>(R.id.buttonAddProduct)
        val addition = findViewById<TextView>(R.id.textViewAddition)
        button.setOnClickListener(View.OnClickListener {
            val names = Functions.getProductListArrayFromDB(this)
            for (i in names) {
                if (i.name == nameInput.text.toString()) {
                    Toast.makeText(
                        this,
                        getString(R.string.product_already_created),
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnClickListener
                }
            }
            if (unitInput.selectedItemPosition == 0) {
                Toast.makeText(
                    this,
                    getString(R.string.no_unit_type_product),
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }
            if (nameInput.text.toString() == "") {
                Toast.makeText(this, getString(R.string.no_product_name), Toast.LENGTH_LONG)
                    .show()
                return@OnClickListener
            }
            val db = DB.ProductListDBAdapter(this).writableDatabase
            DB.ProductDBAdapter(this, Functions.stringToSQLLite3(nameInput.text.toString())).writableDatabase
            val contentValues = ContentValues()
            contentValues.put(
                DB.ProductListDB.TABLE_NAME,
                Functions.stringToSQLLite3(nameInput.text.toString())
            )
            contentValues.put(DB.ProductListDB.TABLE_UNIT, unitInput.selectedItemPosition)
            contentValues.put(DB.ProductListDB.TABLE_ADDITION, Functions.stringToSQLLite3(addition.text.toString()))
            db.insert(DB.ProductListDB.name, null, contentValues)
            val intent = Intent(this, ProductObjAdder::class.java)
            intent.putExtra("name", nameInput.text.toString())
            intent.putExtra("unit", resources.getStringArray(R.array.units)[unitInput.selectedItemPosition])
            startActivity(intent)
        })

    }

}