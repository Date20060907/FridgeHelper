package com.example.fridgehelper.products

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fridgehelper.DB
import com.example.fridgehelper.Functions
import com.example.fridgehelper.Products
import com.example.fridgehelper.R

class ProductEdit : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        val i = Products::class.java
        startActivity(Intent(applicationContext, i))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_edit)

        val name = intent.getStringExtra("name")
        val unit = intent.getStringExtra("unit")

        val deleteButton = findViewById<Button>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this@ProductEdit)
                .setTitle("Delete product")
                .setMessage(getString(R.string.delete_product_question))
                .setPositiveButton(
                    android.R.string.yes
                ) { dialog, which ->
                    DB.ProductListDBAdapter(this@ProductEdit).writableDatabase.delete(
                        DB.ProductListDB.name,
                        "${DB.ProductListDB.TABLE_NAME} =?",
                        arrayOf(Functions.stringToSQLLite3(name.toString()))
                    )
                    val path = this@ProductEdit.getDatabasePath(
                        Functions.stringToSQLLite3(name.toString()) + ".db"
                    )
                    path.delete()
                    startActivity(Intent(applicationContext, Products::class.java))
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        val nameInput = findViewById<EditText>(R.id.NameInput)
        val unitInput = findViewById<Spinner>(R.id.UnitInput)
        val addition = findViewById<TextView>(R.id.textViewEditAddition)

        nameInput.setText(name)
        for (i in 0 until resources.getStringArray(R.array.units).size) {
            if (resources.getStringArray(R.array.units)[i] == unit) {
                unitInput.setSelection(i)
            }
        }

        val addProductButton = findViewById<Button>(R.id.buttonAddProduct)
        addProductButton.setOnClickListener {
            if (unitInput.selectedItemPosition == 0) {
                Toast.makeText(
                    this,
                    "You has not chased unit type for this product",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (nameInput.text.toString() == "") {
                Toast.makeText(this, "You has not wrote name for this product", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            val db = DB.ProductListDBAdapter(applicationContext).writableDatabase
            val updateQuery =
                "UPDATE ${DB.ProductListDB.name} SET ${DB.ProductListDB.TABLE_NAME} = ?, ${DB.ProductListDB.TABLE_UNIT} = ?, ${DB.ProductListDB.TABLE_ADDITION} = ? WHERE ${DB.ProductListDB.TABLE_NAME} = ?"
            db.execSQL(
                updateQuery,
                arrayOf(
                    Functions.stringToSQLLite3(nameInput.text.toString()),
                    unitInput.selectedItemPosition,
                    Functions.stringToSQLLite3(addition.text.toString()),
                    Functions.stringToSQLLite3(name.toString())
                )
            )
            val i = Products::class.java
            startActivity(Intent(applicationContext, i))
        }
    }
}