package com.example.fridgehelper.recipes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.fridgehelper.*


fun getSuggestions(text: String, context: Context): MutableList<String> {
    val products = Functions.getProductListArrayFromDB(context)
    val suggestions: MutableList<String> = ArrayList()
    for (i in products) {
        if (text.length > i.name.length) {
            continue
        }

        var flag = true
        for (j in text.indices) {
            if (text[j].lowercase() != i.name[j].lowercase()) {
                flag = false
                break
            }
        }
        if (flag) {
            suggestions.add(i.name)
        }
    }
    return suggestions
}

class AddRecipe : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Recipes::class.java
        startActivity(Intent(applicationContext, i))
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val nameInput = findViewById<EditText>(R.id.editTextRecipeName)
        val productsInput = findViewById<AutoCompleteTextView>(R.id.editTextProductInput)
        val quantityInput = findViewById<EditText>(R.id.editTextRecipeProductQuantity)
        val unitInput = findViewById<Spinner>(R.id.spinnerRecipeProductUnit)
        val productsOutput = findViewById<TextView>(R.id.textViewReceipeProducts)
        val guideInput = findViewById<EditText>(R.id.editTextRecipeGuide)
        val buttonAddProduct = findViewById<Button>(R.id.buttonAddRecipeProduct)
        val buttonClearProduct = findViewById<Button>(R.id.buttonClearRecipeProducts)
        val buttonAddRecipe = findViewById<Button>(R.id.buttonAddRecipe)

        val productsArray = ArrayList<ArrayList<String>>()
        var hints: MutableList<String> = arrayListOf()


        buttonAddProduct.setOnClickListener {
            if (unitInput.selectedItemPosition == 0) {
                Toast.makeText(
                    this,
                    getString(R.string.no_unit_type_product),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (productsInput.text.toString() == "") {
                Toast.makeText(this, getString(R.string.no_name_product), Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            productsArray.add(
                arrayListOf(
                    productsInput.text.toString().replace(" ", "_"),
                    quantityInput.text.toString(),
                    unitInput.selectedItemPosition.toString()
                )
            )
            productsOutput.text = ""
            for (i in productsArray) {
                if (productsOutput.text.toString() == "") {
                    productsOutput.text = "${
                        i[0].replace(
                            "_",
                            " "
                        )
                    } ${i[1]}${resources.getStringArray(R.array.units)[i[2].toInt()]}"
                } else {
                    productsOutput.text = productsOutput.text.toString() + ", ${
                        i[0].replace(
                            "_",
                            " "
                        )
                    } ${i[1]}${resources.getStringArray(R.array.units)[i[2].toInt()]}"
                }
            }

            productsInput.text.clear()
            quantityInput.text.clear()
        }
        buttonClearProduct.setOnClickListener {
            productsArray.clear()
            productsOutput.text = ""

            productsInput.text.clear()
            quantityInput.text.clear()
        }

        buttonAddRecipe.setOnClickListener {
            if (unitInput.selectedItemPosition == 0) {
                Toast.makeText(
                    this,
                    getString(R.string.no_unit_type_product),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (productsInput.text.toString() == "") {
                Toast.makeText(this, getString(R.string.no_name_product), Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            productsArray.add(
                arrayListOf(
                    productsInput.text.toString().replace(" ", "_"),
                    quantityInput.text.toString(),
                    unitInput.selectedItemPosition.toString()
                )
            )
            productsOutput.text = ""
            for (i in productsArray) {
                if (productsOutput.text.toString() == "") {
                    productsOutput.text = "${
                        i[0].replace(
                            "_",
                            " "
                        )
                    } ${i[1]}${resources.getStringArray(R.array.units)[i[2].toInt()]}"
                } else {
                    productsOutput.text = productsOutput.text.toString() + ", ${
                        i[0].replace(
                            "_",
                            " "
                        )
                    } ${i[1]}${resources.getStringArray(R.array.units)[i[2].toInt()]}"
                }
            }

            val dbRecipes = DB.RecipeDBAdapter(this).writableDatabase
            val cvRecipes = ContentValues()
            val nameRecipe = nameInput.text.toString()

            val names = Functions.getRecipeListArrayFromDB(applicationContext)

            for (i in names) {
                if (i.name == nameRecipe) {
                    Toast.makeText(
                        this,
                        getString(R.string.recipe_already_created),
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
            }

            cvRecipes.put(DB.RecipeDB.TABLE_NAME, Functions.stringToSQLLite3(nameRecipe))
            cvRecipes.put(
                DB.RecipeDB.TABLE_GUIDE,
                Functions.stringToSQLLite3(guideInput.text.toString())
            )
            dbRecipes.insert(DB.RecipeDB.TABLE_TITLE, null, cvRecipes)

            val dbRecipeProducts =
                DB.RecipeProductsDBAdapter(
                    this,
                    Functions.stringToSQLLite3(nameRecipe)
                ).writableDatabase


            for (i in productsArray) {
                val cvProduct = ContentValues()
                cvProduct.put(DB.RecipeProductsDB.TABLE_NAME, Functions.stringToSQLLite3(i[0]))
                cvProduct.put(DB.RecipeProductsDB.TABLE_QUANTITY, i[1])
                cvProduct.put(DB.RecipeProductsDB.TABLE_UNIT, i[2].toInt())
                dbRecipeProducts.insert(
                    Functions.stringToSQLLite3(nameRecipe + "_products"),
                    null,
                    cvProduct
                )
            }
            val int = Intent(this, Recipes::class.java)
            startActivity(int)


        }
        productsInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(p0: CharSequence, start: Int, before: Int, count: Int) {

                val suggestions = getSuggestions(p0.toString(), applicationContext)
                hints = suggestions
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter(
                        applicationContext,
                        android.R.layout.simple_dropdown_item_1line,
                        suggestions
                    )
                productsInput.setAdapter(adapter)

            }

            override fun afterTextChanged(s: Editable) {}
        })
        productsInput.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in Functions.getProductListArrayFromDB(applicationContext)) {
                    if (i.name == hints[p2]) {
                        unitInput.setSelection(i.unit)
                        break
                    }
                }
            }

        })
    }
}