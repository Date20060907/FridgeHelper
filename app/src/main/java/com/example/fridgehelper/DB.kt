package com.example.fridgehelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.provider.BaseColumns._ID

class DB {
    class ProductListDB : BaseColumns {
        companion object {
            const val name = "product_list"

            const val TABLE_INTEGER = "INTEGER"
            const val TABLE_TEXT = "TEXT"

            const val TABLE_NAME = "NAME"
            const val TABLE_UNIT = "UNIT"
            const val TABLE_ADDITION = "ADDITION"
            fun createCommand(): String {
                return "CREATE TABLE IF NOT EXISTS $name($_ID $TABLE_INTEGER PRIMARY KEY AUTOINCREMENT, $TABLE_NAME $TABLE_TEXT, $TABLE_UNIT $TABLE_INTEGER, $TABLE_ADDITION, $TABLE_TEXT)"
            }

            fun dropCommand(): String {
                return "DROP TABLE IF EXIST $name";
            }
        }
    }

    class ProductListDBAdapter(context: Context?) :
        SQLiteOpenHelper(context, "${ProductListDB.name}.db", null, 1) {


        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(ProductListDB.createCommand())
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0?.execSQL(ProductListDB.dropCommand())
            onCreate(p0)
        }
    }

    class ProductDB : BaseColumns {
        companion object {

            const val TABLE_FLOAT = "REAL"
            const val TABLE_INTEGER = "INTEGER"

            const val TABLE_QUANTITY = "QUANTITY"
            const val TABLE_DATE = "DATE"
            fun createCommand(name: String): String {
                return "CREATE TABLE IF NOT EXISTS $name($_ID $TABLE_INTEGER PRIMARY KEY AUTOINCREMENT, $TABLE_QUANTITY $TABLE_FLOAT, $TABLE_DATE $TABLE_INTEGER)"
            }

            fun dropCommand(name: String): String {
                return "DROP TABLE IF EXIST $name"

            }
        }
    }

    class ProductDBAdapter(context: Context?, val name: String?) :
        SQLiteOpenHelper(
            context,
            "${Functions.stringToSQLLite3(Functions.SQLLite3ToString(name.toString()))}.db",
            null,
            1
        ) {

        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(
                ProductDB.createCommand(
                    Functions.stringToSQLLite3(
                        Functions.SQLLite3ToString(
                            name.toString()
                        )
                    )
                )
            )
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0?.execSQL(
                ProductDB.dropCommand(
                    Functions.stringToSQLLite3(
                        Functions.SQLLite3ToString(
                            name.toString()
                        )
                    )
                )
            )
            onCreate(p0)
        }

    }


    class RecipeDB : BaseColumns {
        companion object {
            const val TABLE_NAME = "NAME"
            const val TABLE_GUIDE = "GUIDE"
            const val TABLE_INTEGER = "INTEGER"
            const val TABLE_TEXT = "TEXT"
            const val TABLE_TITLE = "Recipe"
            fun createCommand(): String {
                return "CREATE TABLE IF NOT EXISTS $TABLE_TITLE($_ID $TABLE_INTEGER PRIMARY KEY AUTOINCREMENT, $TABLE_NAME $TABLE_TEXT, $TABLE_GUIDE $TABLE_TEXT)"
            }

            fun dropCommand(): String {
                return "DROP TABLE IF EXIST $TABLE_TITLE"
            }
        }
    }

    class RecipeDBAdapter(context: Context?) :
        SQLiteOpenHelper(context, "${RecipeDB.TABLE_TITLE}.db", null, 1) {
        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(RecipeDB.createCommand())
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0?.execSQL(RecipeDB.dropCommand())
            onCreate(p0)
        }
    }

    class RecipeProductsDB : BaseColumns {
        companion object {
            val TABLE_UNIT = "UNIT"
            val TABLE_NAME = "NAME"
            val TABLE_QUANTITY = "QUANTITY"

            val TABLE_INTEGER = "INTEGER"
            val TABLE_TEXT = "TEXT"
            val TABLE_REAL = "REAL"
            fun createCommand(name: String): String {
                return "CREATE TABLE IF NOT EXISTS $name($_ID $TABLE_INTEGER PRIMARY KEY AUTOINCREMENT, $TABLE_NAME $TABLE_TEXT, $TABLE_QUANTITY $TABLE_REAL, $TABLE_UNIT $TABLE_INTEGER)"
            }

            fun dropCommand(name: String): String {
                return "DROP TABLE IF EXIST $name"
            }
        }
    }

    class RecipeProductsDBAdapter(
        context: Context?, val name: String?
    ) : SQLiteOpenHelper(
        context,
        Functions.stringToSQLLite3("${Functions.SQLLite3ToString(name.toString())}_products") + ".db",
        null,
        1
    ) {
        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(
                RecipeProductsDB.createCommand(
                    Functions.stringToSQLLite3(Functions.SQLLite3ToString(name.toString()) + "_products")
                )
            )
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0?.execSQL(
                RecipeProductsDB.dropCommand(
                    Functions.stringToSQLLite3(Functions.SQLLite3ToString(name.toString()) + "_products")
                )
            )
            onCreate(p0)
        }
    }
}
