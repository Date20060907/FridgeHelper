package com.example.fridgehelper

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

object Functions {
    @SuppressLint("SimpleDateFormat")
    fun dateOutput(date: Calendar): String {
        val format = SimpleDateFormat("yyyy.MM.dd")
        format.calendar = date
        return format.format(date.time)
    }

    fun longToCalendar(date: Long): Calendar {
        if (date == (-1).toLong()) {
            return GregorianCalendar(2999, 12, 31)
        }
        val calendar = GregorianCalendar()
        calendar.timeInMillis = date
        return calendar
    }

    fun stringToSQLLite3(p0: String): String {
        return "\'${p0}\'"
    }

    fun SQLLite3ToString(p0: String): String {
        return p0.replace("\'", "")
    }

    fun findMinDate(array: ArrayList<ProductData>): Long {
        if (array.size == 0) {
            return -1
        }
        var date = Long.MAX_VALUE
        for (i in array) {
            if (i.date < date && i.date != (-1).toLong()) {
                date = i.date
            }
        }
        return if (date != Long.MAX_VALUE) date else (-1)
    }

    fun sumQuantity(array: ArrayList<ProductData>): Float {
        var sum = 0f
        for (i in array) {
            sum += i.quantity
        }
        return sum
    }

    @SuppressLint("Recycle", "Range")
    fun getProductListArrayFromDB(context: Context): ArrayList<BareProduct> {
        val arrayList = ArrayList<BareProduct>()
        val db = DB.ProductListDBAdapter(context).readableDatabase
        val cursor = db.query(DB.ProductListDB.name, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            arrayList.add(
                BareProduct(
                    SQLLite3ToString(cursor.getString(cursor.getColumnIndex(DB.ProductListDB.TABLE_NAME))),
                    cursor.getInt(cursor.getColumnIndex(DB.ProductListDB.TABLE_UNIT)),
                    SQLLite3ToString(cursor.getString(cursor.getColumnIndex(DB.ProductListDB.TABLE_ADDITION)))
                )
            )
        }
        return arrayList
    }

    @SuppressLint("Recycle", "Range")
    fun getProductDB(context: Context, name: String): ArrayList<ProductData> {
        val arrayList = ArrayList<ProductData>()
        val db = DB.ProductDBAdapter(context, stringToSQLLite3(SQLLite3ToString(name))).readableDatabase
        val cursor = db.query(
            stringToSQLLite3(name),
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            arrayList.add(
                ProductData(
                    cursor.getLong(cursor.getColumnIndex(DB.ProductDB.TABLE_DATE)),
                    cursor.getFloat(cursor.getColumnIndex(DB.ProductDB.TABLE_QUANTITY))
                )
            )
        }
        return arrayList
    }

    @SuppressLint("Recycle", "Range")
    fun getRecipeListArrayFromDB(context: Context): ArrayList<Recipe> {
        val arrayList = ArrayList<Recipe>()
        val db = DB.RecipeDBAdapter(context).readableDatabase
        val cursor = db.query(DB.RecipeDB.TABLE_TITLE, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val products = getRecipeProductsListArrayFromDB(
                context,
                SQLLite3ToString(cursor.getString(cursor.getColumnIndex(DB.RecipeDB.TABLE_NAME)))
            )

            arrayList.add(
                Recipe(
                    SQLLite3ToString(cursor.getString(cursor.getColumnIndex(DB.RecipeDB.TABLE_NAME))),
                    products,
                    SQLLite3ToString(cursor.getString(cursor.getColumnIndex(DB.RecipeDB.TABLE_GUIDE)))
                )
            )
        }
        return arrayList
    }

    @SuppressLint("Recycle", "Range")
    fun getRecipeProductsListArrayFromDB(
        context: Context,
        name: String?
    ): ArrayList<RecipeProducts> {
        val arrayList = ArrayList<RecipeProducts>()
        val db = DB.RecipeProductsDBAdapter(context, stringToSQLLite3(name.toString())).readableDatabase
        val cursor = db.query(stringToSQLLite3("${name}_products"), null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            arrayList.add(
                RecipeProducts(
                    SQLLite3ToString(cursor.getString(cursor.getColumnIndex(DB.RecipeProductsDB.TABLE_NAME))),
                    cursor.getFloat(cursor.getColumnIndex(DB.RecipeProductsDB.TABLE_QUANTITY)),
                    cursor.getInt(cursor.getColumnIndex(DB.RecipeProductsDB.TABLE_UNIT)),
                )
            )
        }
        return arrayList
    }

    class BareProduct(val name: String, val unit: Int, val addition: String) {
    }

    class ProductData(val date: Long, val quantity: Float) {

    }

    class Recipe(val name: String, val products: ArrayList<RecipeProducts>, val guide: String) {
    }

    class RecipeProducts(val name: String, val quantity: Float, val unit: Int) {
    }

}