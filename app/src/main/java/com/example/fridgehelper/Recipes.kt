package com.example.fridgehelper

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgehelper.products.AddProductObj
import com.example.fridgehelper.products.Product
import com.example.fridgehelper.products.ProductAdapter
import com.example.fridgehelper.recipes.AddRecipe
import com.example.fridgehelper.recipes.Recipe
import com.example.fridgehelper.recipes.RecipesAdapter
import com.example.fridgehelper.recipes.getSuggestions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.min

class Recipes : AppCompatActivity() {

    fun getSuggestionsRecipeName(p0: String, p1: Context): MutableList<Functions.Recipe> {
        val recipeNames = Functions.getRecipeListArrayFromDB(p1)
        val output = ArrayList<Functions.Recipe>()
        for (i in recipeNames) {
            var flag = true
            for (n in p0.indices) {
                if (i.name.length <= n || p0.lowercase()[n] != i.name.lowercase()[n]) {
                    flag = false
                    break
                }
            }
            if (flag) {
                output.add(i)
            }
        }
        return output
    }

    fun getSuggestionsProducts(p0: String, p1: Context): ArrayList<Functions.Recipe> {
        val recipeNames = Functions.getRecipeListArrayFromDB(p1)
        val output = ArrayList<Functions.Recipe>()
        for (i in recipeNames) {
            var flag = true
            val recipeProducts = Functions.getRecipeProductsListArrayFromDB(p1, i.name)
            for (j in recipeProducts) {
                for (n in p0.indices) {
                    if (j.name.length <= n || p0.lowercase()[n] != j.name.lowercase()[n]) {
                        flag = false
                        break
                    }
                }
                if (flag) {
                    output.add(i)
                    break
                }
            }
        }
        return output
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = MainActivity::class.java
        startActivity(Intent(applicationContext, i))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)
        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButtonAddRecipe)
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerViewRecipes)
        val sortSpinner = findViewById<Spinner>(R.id.recipeSortingSpinner)
        val sortingText = findViewById<AutoCompleteTextView>(R.id.sortingText)
        sortSpinner.onItemSelectedListener
        var sortType: Int = sortSpinner.selectedItemPosition
        var hints: MutableList<String> = arrayListOf()

        addButton.setOnClickListener {
            val intentRecipes = Intent(this, AddRecipe::class.java)
            startActivity(intentRecipes)
        }
        var arrayList = Functions.getRecipeListArrayFromDB(this)

        fun minDate(i: Functions.RecipeProducts, m: Long): Long {
            val a = Functions.findMinDate(
                Functions.getProductDB(
                    applicationContext, i.name
                )
            )
            if (a == (-1).toLong()) {
                return m;
            } else {
                return min(a, m)
            }
        }

        fun SortArray(sortType: Int) {
            when (sortType) {
                0 -> {
                    arrayList.sortBy { it.name }
                    sortingText.visibility = View.VISIBLE
                }
                1 -> {
                    arrayList.sortWith(object : Comparator<Functions.Recipe> {
                        override fun compare(p0: Functions.Recipe?, p1: Functions.Recipe?): Int {
                            var min1: Long = Long.MAX_VALUE
                            var min2: Long = Long.MAX_VALUE
                            if (p0 != null) {
                                for (i in p0.products) {
                                    if (getDatabasePath(Functions.stringToSQLLite3(i.name)) != null) {
                                        min1 = minDate(i, min1)
                                    }
                                }
                            }
                            if (min1 == Long.MAX_VALUE) return 1
                            if (p1 != null) {
                                for (i in p1.products) {
                                    if (getDatabasePath(Functions.stringToSQLLite3(i.name)) != null) {
                                        min2 = minDate(i, min2)
                                    }
                                }
                            }
                            if (min2 == Long.MAX_VALUE) return -1
                            return if (min1 == min2) 0
                            else if (min1 < min2) -1 else 1
                        }


                    })
                    sortingText.visibility = View.INVISIBLE
                }
                2 -> {
                    arrayList.sortWith(object : Comparator<Functions.Recipe> {
                        override fun compare(p0: Functions.Recipe?, p1: Functions.Recipe?): Int {
                            var is1: Long = 0
                            var is2: Long = 0
                            var n1: Long = 0
                            var n2: Long = 0
                            if (p0 != null) {
                                for (i in p0.products) {
                                    if (getDatabasePath(Functions.stringToSQLLite3(i.name)) != null) {
                                        val a = Functions.getProductDB(
                                            applicationContext, i.name
                                        )
                                        if (a.size == 0) n1++ else is1++
                                    }
                                }
                            }
                            if (p1 != null) {
                                for (i in p1.products) {
                                    if (getDatabasePath(Functions.stringToSQLLite3(i.name)) != null) {
                                        val a = Functions.getProductDB(
                                            applicationContext, i.name
                                        )
                                        if (a.size == 0) n2++ else is2++;
                                    }
                                }
                            }
                            if (n1 == n2){
                                if (is1 == is2) return 0
                                return if (is1 > is2) -1 else 1
                            }else if (n1 < n2) return -1 else return 1


                        }
                    })
                    sortingText.visibility = View.VISIBLE

                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)


        val recipeClickListener: RecipesAdapter.onRecipeClickListener =
            object : RecipesAdapter.onRecipeClickListener {

                override fun onRecipeClick(recipe: Functions.Recipe, position: Int) {
                    val intent = Intent(applicationContext, Recipe::class.java)
                    intent.putExtra("name", recipe.name)
                    intent.putExtra("guide", recipe.guide)
                    startActivity(intent)
                }
            }
        val recipeLongClickListener: RecipesAdapter.onRecipeLongClickListener =
            object : RecipesAdapter.onRecipeLongClickListener {
                override fun onRecipeLongClick(recipe: Functions.Recipe, position: Int) {
                    AlertDialog.Builder(this@Recipes).setTitle("Delete recipe")
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton(
                            android.R.string.yes
                        ) { dialog, which ->
                            DB.RecipeDBAdapter(this@Recipes).writableDatabase.delete(
                                DB.RecipeDB.TABLE_TITLE,
                                "${DB.RecipeDB.TABLE_NAME} =?",
                                arrayOf(Functions.stringToSQLLite3(recipe.name))
                            )
                            val path = this@Recipes.getDatabasePath(
                                Functions.stringToSQLLite3(recipe.name + "_products") + ".db"
                            )
                            path.delete()
                            startActivity(intent)
                        }.setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert).show()
                }

            }

        var adapter = RecipesAdapter(arrayList, recipeClickListener, recipeLongClickListener)
        recyclerView.adapter = adapter

        sortSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sortType = p2
                sortingText.text.clear()
                arrayList = Functions.getRecipeListArrayFromDB(applicationContext)
                SortArray(p2)
                adapter = RecipesAdapter(arrayList, recipeClickListener, recipeLongClickListener)
                recyclerView.adapter = adapter
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        sortingText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when (sortType) {
                    0 -> {
                        if (p0 != null) {
                            if (p0.isNotEmpty()) {
                                val suggestions = getSuggestionsRecipeName(
                                    p0.toString(), applicationContext
                                )
                                val array = ArrayList<Functions.Recipe>()
                                hints.clear()
                                for (i in suggestions) {
                                    hints.add(i.name)
                                }

                                val adapter1: ArrayAdapter<String> = ArrayAdapter(
                                    applicationContext,
                                    android.R.layout.simple_dropdown_item_1line,
                                    hints
                                )
                                for (i in suggestions) {
                                    array.add(i)
                                }
                                sortingText.setAdapter(adapter1)
                                arrayList = array
                                SortArray(0)
                                adapter = RecipesAdapter(
                                    arrayList, recipeClickListener, recipeLongClickListener
                                )
                                recyclerView.adapter = adapter
                            } else {
                                arrayList = Functions.getRecipeListArrayFromDB(applicationContext)
                                SortArray(0)
                                adapter = RecipesAdapter(
                                    arrayList, recipeClickListener, recipeLongClickListener
                                )
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                    2 -> {
                        if (p0 != null) {
                            if (p0.isNotEmpty()) {
                                val suggestions = getSuggestionsProducts(
                                    p0.toString(), applicationContext
                                )
                                arrayList = suggestions
                                SortArray(2)
                                adapter = RecipesAdapter(
                                    arrayList, recipeClickListener, recipeLongClickListener
                                )
                                recyclerView.adapter = adapter
                            } else {
                                arrayList = Functions.getRecipeListArrayFromDB(applicationContext)
                                SortArray(0)
                                adapter = RecipesAdapter(
                                    arrayList, recipeClickListener, recipeLongClickListener
                                )
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}
